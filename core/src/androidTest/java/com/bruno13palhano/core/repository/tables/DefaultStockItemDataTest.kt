package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.product.DefaultProductData
import com.bruno13palhano.core.data.repository.stock.DefaultStockData
import com.bruno13palhano.core.data.repository.stock.StockData
import com.bruno13palhano.core.mocks.makeRandomDataVersion
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.mocks.makeRandomStockOrder
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.StockItem
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DefaultStockItemDataTest {
    @Inject
    lateinit var database: ShopDatabase
    private lateinit var dataVersion: DataVersion
    private lateinit var stockItemTable: StockData
    private lateinit var firstItem: StockItem
    private lateinit var secondItem: StockItem
    private lateinit var thirdItem: StockItem
    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var product3: Product

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun setup() =
        runTest {
            hiltTestRule.inject()

            stockItemTable =
                DefaultStockData(
                    database.stockTableQueries,
                    database.versionTableQueries,
                    Dispatchers.IO,
                )

            val productTable =
                DefaultProductData(
                    database.shopDatabaseQueries,
                    database.productCategoriesTableQueries,
                    database.versionTableQueries,
                    Dispatchers.IO,
                )

            dataVersion = makeRandomDataVersion(id = 1L)
            product1 =
                makeRandomProduct(
                    id = 1L,
                    name = "Homem",
                    categories = listOf(Category(id = 1L, category = "Perfumes", timestamp = "")),
                )
            product2 =
                makeRandomProduct(
                    id = 2L,
                    name = "Kaiak",
                    categories =
                        listOf(
                            Category(id = 2L, category = "Soaps", timestamp = ""),
                            Category(id = 1L, category = "Perfumes", timestamp = ""),
                        ),
                )
            product3 =
                makeRandomProduct(
                    id = 3L,
                    name = "Luna",
                    categories =
                        listOf(
                            Category(id = 3L, category = "Others", timestamp = ""),
                        ),
                )
            productTable.insert(product1, dataVersion, {}, {})
            productTable.insert(product2, dataVersion, {}, {})
            productTable.insert(product3, dataVersion, {}, {})

            firstItem = makeRandomStockOrder(id = 1L, product = product1, purchasePrice = 120.90F, isPaid = false)
            secondItem = makeRandomStockOrder(id = 2L, product = product2, purchasePrice = 150.99F, isPaid = false)
            thirdItem = makeRandomStockOrder(id = 3L, product = product3, purchasePrice = 210.80F, isPaid = true)
        }

    @Test
    fun shouldInsertStockOrderInTheDatabase() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).contains(firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldUpdateStockOrderInTheDatabase_ifStockOrderExists() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})

            val updatedStockItem =
                makeRandomStockOrder(
                    id = 1L,
                    product = product1,
                )
            stockItemTable.update(updatedStockItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).contains(updatedStockItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotUpdateStockOrderInTheDatabase_ifStockOrderNotExists() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.update(secondItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).doesNotContain(secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItems_ifDatabaseNotEmpty() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getItems().collect {
                    assertThat(it).containsExactly(firstItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoStockItemsInTheDatabase() =
        runTest {
            launch(Dispatchers.IO) {
                stockItemTable.getItems().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItems_matchesWithThisSearch_ifDatabaseNotEmpty() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.search(value = product1.name).collect {
                    assertThat(it).containsExactly(firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItems_ifSearchIsEmpty_andDatabaseNotEmpty() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.search(value = "").collect {
                    assertThat(it).containsExactly(firstItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoMatchesWithThisSearch() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.search(value = " ").collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItems_matchesWithThisCategory_ifDatabaseNotEmpty() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getByCategory(category = "Perfumes").collect {
                    assertThat(it).containsExactly(firstItem, secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoItemsInThisCategory() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getByCategory(category = "test").collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItems_ifThisCategoryValueIsEmpty() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getByCategory(category = "").collect {
                    assertThat(it).containsExactly(firstItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldUpdateStockItemQuantityInTheDatabase_ifExists() =
        runTest {
            val quantity = 10

            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.updateStockQuantity(id = firstItem.id, quantity = quantity)

            launch(Dispatchers.IO) {
                stockItemTable.getById(id = firstItem.id).collect {
                    assertThat(it.quantity).isEqualTo(quantity)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotUpdateStockItemQuantityInTheDatabase_ifNotExists() =
        runTest {
            val quantity = 10

            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.updateStockQuantity(id = secondItem.id, quantity = quantity)

            launch(Dispatchers.IO) {
                stockItemTable.getById(id = secondItem.id).collect {
                    assertThat(it.quantity).isNotEqualTo(quantity)
                    cancel()
                }
            }
        }

    @Test
    fun shouldDeleteStockItemWithThisIdInTheDatabase_ifStockOrderExists() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})

            stockItemTable.deleteById(firstItem.id, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).doesNotContain(firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotDeleteStockOrderWithThisIdInTheDatabase_ifStockOrderNotExists() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})

            stockItemTable.deleteById(thirdItem.id, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).containsExactly(firstItem, secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItemsInTheDatabase_ifDatabaseNotEmpty() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).containsExactly(firstItem, secondItem, thirdItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() =
        runTest {
            launch(Dispatchers.IO) {
                stockItemTable.getAll().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItemsNotPaid_ifDatabaseNotEmpty() =
        runTest {
            stockItemTable.insert(firstItem, dataVersion, {}, {})
            stockItemTable.insert(secondItem, dataVersion, {}, {})
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getDebitStock().collect {
                    assertThat(it).containsExactly(firstItem, secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoDebitInDatabase() =
        runTest {
            stockItemTable.insert(thirdItem, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getDebitStock().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllItemsOutOfStock_ifDatabaseNotEmpty() =
        runTest {
            val item1 = makeRandomStockOrder(id = 1L, product = product1, quantity = 10)
            val item2 = makeRandomStockOrder(id = 2L, product = product2, quantity = 0)
            val item3 = makeRandomStockOrder(id = 3L, product = product3, quantity = 0)
            stockItemTable.insert(item1, dataVersion, {}, {})
            stockItemTable.insert(item2, dataVersion, {}, {})
            stockItemTable.insert(item3, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getOutOfStock().collect {
                    assertThat(it).containsExactly(item2, item3)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoItemsOutOfStockInDatabase() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getOutOfStock().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockItems_ifDatabaseIsNotEmpty() =
        runTest {
            val item1 = makeRandomStockOrder(id = 1L, product = product1, quantity = 10)
            val item2 = makeRandomStockOrder(id = 2L, product = product2, quantity = 0)
            val item3 = makeRandomStockOrder(id = 3L, product = product3, quantity = 12)
            stockItemTable.insert(item1, dataVersion, {}, {})
            stockItemTable.insert(item2, dataVersion, {}, {})
            stockItemTable.insert(item3, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                stockItemTable.getStockItems().collect {
                    assertThat(it).containsExactly(item1, item3)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoStockItemInDatabase() =
        runTest {
            launch(Dispatchers.IO) {
                stockItemTable.getStockItems().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllDebitItemOrderedByPriceDesc_ifNotEmpty() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getDebitStockByPrice(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(secondItem, firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllDebitItemOrderedByPriceAsc_ifNotEmpty() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getDebitStockByPrice(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(firstItem, secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllDebitItemOrderedByNameDesc_ifNotEmpty() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getDebitStockByName(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(firstItem, secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllDebitItemOrderedByNameAsc_ifNotEmpty() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getDebitStockByName(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(secondItem, firstItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnStockItemWithThisId() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getById(id = secondItem.id).collect {
                    assertThat(it).isEqualTo(secondItem)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnLastItemInsertedInDatabase() =
        runTest {
            insertAllItems()
            launch(Dispatchers.IO) {
                stockItemTable.getLast().collect {
                    assertThat(it).isEqualTo(thirdItem)
                    cancel()
                }
            }
        }

    private suspend fun insertAllItems() {
        stockItemTable.insert(firstItem, dataVersion, {}, {})
        stockItemTable.insert(secondItem, dataVersion, {}, {})
        stockItemTable.insert(thirdItem, dataVersion, {}, {})
    }
}
