package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.catalog.DefaultCatalogData
import com.bruno13palhano.core.data.repository.product.DefaultProductData
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
class DefaultCatalogDataTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var catalogTable: CatalogRepository<Catalog>
    private lateinit var firstItem: Catalog
    private lateinit var secondItem: Catalog
    private lateinit var thirdItem: Catalog

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() = runBlocking {
        hiltTestRule.inject()

        val productTable = DefaultProductData(
            database.shopDatabaseQueries,
            database.productCategoriesTableQueries,
            Dispatchers.IO
        )
        catalogTable = DefaultCatalogData(database.catalogTableQueries, Dispatchers.IO)

        val product1 = makeRandomProduct(id = 1L, name = "Homem")
        val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, name = "Luna")
        productTable.insert(product1)
        productTable.insert(product2)
        productTable.insert(product3)

        firstItem = makeRandomCatalog(id = 1L, productId = 1L, name = product1.name, price = 100F)
        secondItem = makeRandomCatalog(id = 2L, productId = 2L, name = product2.name, price = 50F)
        thirdItem = makeRandomCatalog(id = 3L, productId = 3L, name = product3.name, price = 25F)
    }

    @Test
    fun shouldInsertCatalogItemInTheDatabase() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getAll().collect { items ->
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

        insertAllCatalogs()
        catalogTable.update(updatedItem)

        launch(Dispatchers.IO) {
            catalogTable.getAll().collect { items ->
                assertThat(items).containsExactly(updatedItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateCatalogItemInTheDatabase_ifCatalogItemNotExists() = runTest {
        insertTwoCatalogs()
        catalogTable.update(thirdItem)
        launch {
            catalogTable.getAll().collect { items ->
                assertThat(items).doesNotContain(thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemExists() = runTest {
        insertAllCatalogs()
        catalogTable.deleteById(firstItem.id)

        launch(Dispatchers.IO) {
            catalogTable.getAll().collect { items ->
                assertThat(items).doesNotContain(firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemNotExists() = runTest {
        insertTwoCatalogs()
        catalogTable.deleteById(thirdItem.id)

        launch(Dispatchers.IO) {
            catalogTable.getAll().collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllCatalogItemsInTheDatabase_ifDatabaseIsNotEmpty() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getAll().collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            catalogTable.getAll().collect { items ->
                assertThat(items).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameDesc() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getOrderedByName(isOrderedAsc = false).collect { items ->
                assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameAsc() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getOrderedByName(isOrderedAsc = true).collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceDesc() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getOrderedByPrice(isOrderedAsc = false).collect { items ->
                assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceAsc() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getOrderedByPrice(isOrderedAsc = true).collect { items ->
                assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCatalogItemWithThisId_ifExists() = runTest {
        insertAllCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getById(firstItem.id).collect { item ->
                assertThat(item).isEqualTo(firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastCatalogItem_ifNotEmpty() = runTest {
        insertTwoCatalogs()
        launch(Dispatchers.IO) {
            catalogTable.getLast().collect { item ->
                assertThat(item).isEqualTo(secondItem)
                cancel()
            }
        }
    }

    private suspend fun insertTwoCatalogs() {
        catalogTable.insert(firstItem)
        catalogTable.insert(secondItem)
    }

    private suspend fun insertAllCatalogs() {
        catalogTable.insert(firstItem)
        catalogTable.insert(secondItem)
        catalogTable.insert(thirdItem)
    }
}