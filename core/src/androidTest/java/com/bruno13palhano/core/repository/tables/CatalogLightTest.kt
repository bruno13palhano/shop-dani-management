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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    fun shouldInsertCatalogItemInTheDatabase() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getAll().collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateCatalogItemInTheDatabase_ifCatalogItemExists() = runTest {
        val updatedItem = makeRandomCatalog(
            id = firstItem.id,
            productId = firstItem.productId,
            name = firstItem.name
        )

        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)
        catalogRepository.update(updatedItem)

        launch(Dispatchers.IO) {
            catalogRepository.getAll().collect { items ->
                assertThat(items).containsExactly(updatedItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateCatalogItemInTheDatabase_ifCatalogItemNotExists() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.update(zeroIdItem)

        launch {
            catalogRepository.getAll().collect { items ->
                assertThat(items).doesNotContain(zeroIdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemExists() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        catalogRepository.deleteById(firstItem.id)

        launch(Dispatchers.IO) {
            catalogRepository.getAll().collect { items ->
                assertThat(items).doesNotContain(firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemNotExists() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        catalogRepository.deleteById(zeroIdItem.id)

        launch(Dispatchers.IO) {
            catalogRepository.getAll().collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllCatalogItemsInTheDatabase_ifDatabaseIsNotEmpty() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getAll().collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            catalogRepository.getAll().collect { items ->
                assertThat(items).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameDesc() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getOrderedByName(isOrderedAsc = false).collect { items ->
                assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameAsc() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getOrderedByName(isOrderedAsc = true).collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceDesc() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getOrderedByPrice(isOrderedAsc = false).collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceAsc() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getOrderedByPrice(isOrderedAsc = true).collect { items ->
                assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemWithThisId_ifExists() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)
        catalogRepository.insert(thirdItem)

        launch(Dispatchers.IO) {
            catalogRepository.getById(firstItem.id).collect { item ->
                assertThat(item).isEqualTo(firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastCatalogItem_ifNotEmpty() = runTest {
        catalogRepository.insert(firstItem)
        catalogRepository.insert(secondItem)

        launch(Dispatchers.IO) {
            catalogRepository.getLast().collect { item ->
                assertThat(item).isEqualTo(secondItem)
                cancel()
            }
        }
    }
}