package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.catalog.CatalogData
import com.bruno13palhano.core.data.repository.catalog.LocalCatalogData
import com.bruno13palhano.core.data.repository.product.LocalProductData
import com.bruno13palhano.core.mocks.makeRandomCatalog
import com.bruno13palhano.core.mocks.makeRandomDataVersion
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.DataVersion
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
class LocalCatalogDataTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var catalogTable: CatalogData
    private lateinit var firstItem: Catalog
    private lateinit var secondItem: Catalog
    private lateinit var thirdItem: Catalog
    private lateinit var dataVersion: DataVersion

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() =
        runBlocking {
            hiltTestRule.inject()

            val productTable =
                LocalProductData(
                    database.shopDatabaseQueries,
                    database.productCategoriesTableQueries,
                    database.versionTableQueries,
                    Dispatchers.IO,
                )
            catalogTable =
                LocalCatalogData(
                    database.catalogTableQueries,
                    database.versionTableQueries,
                    Dispatchers.IO,
                )

            dataVersion = makeRandomDataVersion(id = 1L)
            val product1 = makeRandomProduct(id = 1L, name = "Homem")
            val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
            val product3 = makeRandomProduct(id = 3L, name = "Luna")
            productTable.insert(product1, dataVersion, {}, {})
            productTable.insert(product2, dataVersion, {}, {})
            productTable.insert(product3, dataVersion, {}, {})

            firstItem = makeRandomCatalog(id = 1L, productId = 1L, name = product1.name, price = 100F)
            secondItem = makeRandomCatalog(id = 2L, productId = 2L, name = product2.name, price = 50F)
            thirdItem = makeRandomCatalog(id = 3L, productId = 3L, name = product3.name, price = 25F)
        }

    @Test
    fun shouldInsertCatalogItemInTheDatabase() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getAll().collect { items ->
                    assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldUpdateCatalogItemInTheDatabase_ifCatalogItemExists() =
        runTest {
            val updatedItem =
                makeRandomCatalog(
                    id = firstItem.id,
                    productId = firstItem.productId,
                    name = firstItem.name,
                )

            insertAllCatalogs()
            catalogTable.update(updatedItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                catalogTable.getAll().collect { items ->
                    assertThat(items).containsExactly(updatedItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotUpdateCatalogItemInTheDatabase_ifCatalogItemNotExists() =
        runTest {
            insertTwoCatalogs()
            catalogTable.update(thirdItem, dataVersion, {}, {})
            launch {
                catalogTable.getAll().collect { items ->
                    assertThat(items).doesNotContain(thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemExists() =
        runTest {
            insertAllCatalogs()
            catalogTable.deleteById(firstItem.id, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                catalogTable.getAll().collect { items ->
                    assertThat(items).doesNotContain(firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotDeleteCatalogItemWithThisIdInTheDatabase_ifCatalogItemNotExists() =
        runTest {
            insertTwoCatalogs()
            catalogTable.deleteById(thirdItem.id, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                catalogTable.getAll().collect { items ->
                    assertThat(items).containsExactly(firstItem, secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCatalogItemsInTheDatabase_ifDatabaseIsNotEmpty() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getAll().collect { items ->
                    assertThat(items).containsExactly(firstItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() =
        runTest {
            launch(Dispatchers.IO) {
                catalogTable.getAll().collect { items ->
                    assertThat(items).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameDesc() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getOrderedByName(isOrderedAsc = false).collect { items ->
                    assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCatalogItemsOrderedByNameAsc() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getOrderedByName(isOrderedAsc = true).collect { items ->
                    assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceDesc() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getOrderedByPrice(isOrderedAsc = false).collect { items ->
                    assertThat(items).containsExactly(firstItem, secondItem, thirdItem).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCatalogItemsOrderedByPriceAsc() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getOrderedByPrice(isOrderedAsc = true).collect { items ->
                    assertThat(items).containsExactly(thirdItem, secondItem, firstItem).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCatalogItemWithThisId_ifExists() =
        runTest {
            insertAllCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getById(firstItem.id).collect { item ->
                    assertThat(item).isEqualTo(firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnLastCatalogItem_ifNotEmpty() =
        runTest {
            insertTwoCatalogs()
            launch(Dispatchers.IO) {
                catalogTable.getLast().collect { item ->
                    assertThat(item).isEqualTo(secondItem)
                    cancel()
                }
            }
        }

    private suspend fun insertTwoCatalogs() {
        catalogTable.insert(firstItem, dataVersion, {}, {})
        catalogTable.insert(secondItem, dataVersion, {}, {})
    }

    private suspend fun insertAllCatalogs() {
        catalogTable.insert(firstItem, dataVersion, {}, {})
        catalogTable.insert(secondItem, dataVersion, {}, {})
        catalogTable.insert(thirdItem, dataVersion, {}, {})
    }
}
