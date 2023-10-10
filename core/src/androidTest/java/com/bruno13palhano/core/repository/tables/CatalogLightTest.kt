package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.repository.catalog.CatalogLight
import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.product.ProductLight
import com.bruno13palhano.core.mocks.makeRandomCatalog
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.model.Catalog
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

@HiltAndroidTest
class CatalogLightTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var catalogRepository: CatalogData<Catalog>
    private lateinit var zeroIdItem: Catalog
    private lateinit var firstItem: Catalog
    private lateinit var secondItem: Catalog
    private lateinit var thirdItem: Catalog

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() = runBlocking {
        hiltTestRule.inject()

        val catalogData = CatalogLight(database.catalogTableQueries, Dispatchers.IO)
        val productRepository = ProductLight(
            database.shopDatabaseQueries,
            database.productCategoriesTableQueries,
            Dispatchers.IO
        )
        catalogRepository = CatalogRepository(catalogData)

        val product1 = makeRandomProduct(id = 1L, name = "Homem")
        val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, name = "Luna")
        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        zeroIdItem = makeRandomCatalog(id = 0L, productId = 1L, name = product1.name, price = 10F)
        firstItem = makeRandomCatalog(id = 1L, productId = 1L, name = product1.name, price = 100F)
        secondItem = makeRandomCatalog(id = 2L, productId = 2L, name = product2.name, price = 50F)
        thirdItem = makeRandomCatalog(id = 3L, productId = 3L, name = product3.name, price = 25F)
    }

    @Test
    fun shouldInsertCatalogItemInTheDatabase() = runBlocking {
        val latch = CountDownLatch(1)
        catalogRepository.insert(firstItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).contains(firstItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldUpdateCatalogItemInTheDatabase_ifCatalogItemExists() = runBlocking {
        val latch = CountDownLatch(1)
        val updatedItem = makeRandomCatalog(
            id = firstItem.id,
            productId = firstItem.productId,
            name = firstItem.name
        )

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)
        catalogRepository.update(updatedItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).contains(updatedItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotUpdateCatalogItemInTheDatabase_ifCatalogItemNotExists() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)
        catalogRepository.update(zeroIdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).doesNotContain(zeroIdItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDeleteCatalogItemInTheDatabase_ifCatalogItemExists() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        catalogRepository.delete(firstItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).doesNotContain(firstItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotDeleteCatalogItemInTheDatabase_ifCatalogItemNotExists() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        catalogRepository.delete(zeroIdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemExists() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        catalogRepository.deleteById(firstItem.id)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).doesNotContain(firstItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemNotExists() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        catalogRepository.deleteById(zeroIdItem.id)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).contains(firstItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllCatalogItemsInTheDatabase_ifDatabaseIsNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            catalogRepository.getAll().take(1).collect { items ->
                latch.countDown()
                assertThat(items).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameDesc() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getOrderedByName(isOrderedAsc = false).take(1).collect { items ->
                latch.countDown()
                assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameAsc() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getOrderedByName(isOrderedAsc = true).take(1).collect { items ->
                latch.countDown()
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceDesc() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getOrderedByPrice(isOrderedAsc = false).take(1).collect { items ->
                latch.countDown()
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceAsc() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getOrderedByPrice(isOrderedAsc = true).take(1).collect { items ->
                latch.countDown()
                assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCatalogItemWithThisId_ifExists() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getById(thirdItem.id).collect { item ->
                latch.countDown()
                assertThat(item).isEqualTo(thirdItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnLastCatalogItem_ifNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)

        val job = async(Dispatchers.IO) {
            catalogRepository.getLast().collect { item ->
                latch.countDown()
                assertThat(item).isEqualTo(secondItem)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}