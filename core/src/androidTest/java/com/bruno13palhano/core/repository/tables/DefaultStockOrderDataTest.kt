package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.data.repository.product.DefaultProductData
import com.bruno13palhano.core.data.repository.stockorder.DefaultStockOrderData
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.mocks.makeRandomStockOrder
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.StockOrder
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
class DefaultStockOrderDataTest {
    @Inject
    lateinit var database: ShopDatabase
    private lateinit var stockOrderTable: StockOrderRepository<StockOrder>
    private lateinit var firstItem: StockOrder
    private lateinit var secondItem: StockOrder
    private lateinit var thirdItem: StockOrder
    private lateinit var firstOrder: StockOrder
    private lateinit var secondOrder: StockOrder
    private lateinit var thirdOrder: StockOrder
    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var product3: Product

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun setup() = runTest {
        hiltTestRule.inject()

        stockOrderTable = DefaultStockOrderData(database.stockOrderTableQueries, Dispatchers.IO)

        val productTable = DefaultProductData(
            database.shopDatabaseQueries,
            database.productCategoriesTableQueries,
            Dispatchers.IO
        )
        product1 = makeRandomProduct(
            id = 1L,
            name = "Homem",
            categories = listOf(Category(id = 1L, category = "Perfumes"))
        )
        product2 = makeRandomProduct(
            id = 2L,
            name = "Kaiak",
            categories = listOf(
                Category(id = 2L, category = "Soaps"),
                Category(id = 1L, category = "Perfumes")
            )
        )
        product3 = makeRandomProduct(
            id = 3L,
            name = "Luna",
            categories = listOf(Category(id = 3L, category = "Others")
            )
        )
        productTable.insert(product1)
        productTable.insert(product2)
        productTable.insert(product3)

        firstItem = makeRandomStockOrder(id = 1L, product = product1, purchasePrice = 120.90F, isOrderedByCustomer = false, isPaid = false)
        secondItem = makeRandomStockOrder(id = 2L, product = product2, purchasePrice = 150.99F, isOrderedByCustomer = false, isPaid = false)
        thirdItem = makeRandomStockOrder(id = 3L, product = product3, purchasePrice = 210.80F, isOrderedByCustomer = false, isPaid = true)
        firstOrder = makeRandomStockOrder(id = 1L, product = product1, isOrderedByCustomer = true)
        secondOrder = makeRandomStockOrder(id = 2L, product = product2, isOrderedByCustomer = true)
        thirdOrder = makeRandomStockOrder(id = 3L, product = product3, isOrderedByCustomer = true)
    }

    @Test
    fun shouldInsertStockOrderInTheDatabase() = runTest {
        stockOrderTable.insert(firstItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).contains(firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateStockOrderInTheDatabase_ifStockOrderExists() = runTest {
        stockOrderTable.insert(firstItem)

        val updatedStockItem = makeRandomStockOrder(
            id = 1L,
            product = product1,
            isOrderedByCustomer = false
        )
        stockOrderTable.update(updatedStockItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).contains(updatedStockItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateStockOrderInTheDatabase_ifStockOrderNotExists() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.update(secondItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).doesNotContain(secondItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItems_ifDatabaseNotEmpty() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getItems(isOrderedByCustomer = false).collect {
                assertThat(it).containsExactly(firstItem, secondItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoStockItemsInTheDatabase() = runTest {
        insertAllOrders()
        launch(Dispatchers.IO) {
            stockOrderTable.getItems(isOrderedByCustomer = false).collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllOrders_ifDatabaseNotEmpty() = runTest {
        insertAllOrders()
        launch(Dispatchers.IO) {
            stockOrderTable.getItems(isOrderedByCustomer = true).collect {
                assertThat(it).containsExactly(firstOrder, secondOrder, thirdOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoOrdersInTheDatabase() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getItems(isOrderedByCustomer = true).collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItems_matchesWithThisSearch_ifDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdItem)

        launch(Dispatchers.IO) {
            stockOrderTable.search(value = product1.name, isOrderedByCustomer = false).collect {
                assertThat(it).contains(firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItems_ifSearchIsEmpty_andDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdItem)

        launch(Dispatchers.IO) {
            stockOrderTable.search(value = "", isOrderedByCustomer = false).collect {
                assertThat(it).containsExactly(firstItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllOrders_matchesWithThisSearch_ifDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdOrder)

        launch(Dispatchers.IO) {
            stockOrderTable.search(value = product2.name, isOrderedByCustomer = true).collect {
                assertThat(it).contains(secondOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllOrders_ifSearchIsEmpty_andDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdOrder)

        launch(Dispatchers.IO) {
            stockOrderTable.search(value = "", isOrderedByCustomer = true).collect {
                assertThat(it).containsExactly(secondOrder, thirdOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoMatchesWithThisSearch() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdOrder)

        launch(Dispatchers.IO) {
            stockOrderTable.search(value = " ", isOrderedByCustomer = true).collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItems_matchesWithThisCategory_ifDatabaseNotEmpty() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getByCategory(isOrderedByCustomer = false, category = "Perfumes").collect {
                assertThat(it).containsExactly(firstItem, secondItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoItemsInThisCategory() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getByCategory(isOrderedByCustomer = false, category = "test").collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItems_ifThisCategoryValueIsEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getByCategory(isOrderedByCustomer = false, category = "").collect {
                assertThat(it).containsExactly(firstItem, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllOrders_matchesWithThisCategory_ifDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getByCategory(isOrderedByCustomer = true, category = "Perfumes").collect {
                assertThat(it).contains(secondOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoOrdersInThisCategory() = runTest {
        insertAllOrders()
        launch(Dispatchers.IO) {
            stockOrderTable.getByCategory(isOrderedByCustomer = true, category = "test").collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllOrders_ifThisCategoryValueIsEmpty() = runTest {
        insertAllOrders()
        launch(Dispatchers.IO) {
            stockOrderTable.getByCategory(isOrderedByCustomer = true, category = "").collect {
                assertThat(it).containsExactly(firstOrder, secondOrder, thirdOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateStockOrderQuantityInTheDatabase_ifExists() = runTest {
        val quantity = 10

        stockOrderTable.insert(firstItem)
        stockOrderTable.updateStockOrderQuantity(id = firstItem.id, quantity = quantity)

        launch(Dispatchers.IO) {
            stockOrderTable.getById(id = firstItem.id).collect {
                assertThat(it.quantity).isEqualTo(quantity)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateStockOrderQuantityInTheDatabase_ifNotExists() = runTest {
        val quantity = 10

        stockOrderTable.insert(firstItem)
        stockOrderTable.updateStockOrderQuantity(id = secondItem.id, quantity = quantity)

        launch(Dispatchers.IO) {
            stockOrderTable.getById(id = secondItem.id).collect {
                assertThat(it.quantity).isNotEqualTo(quantity)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteStockOrderWithThisIdInTheDatabase_ifStockOrderExists() = runTest {
        stockOrderTable.insert(firstOrder)
        stockOrderTable.insert(secondOrder)

        stockOrderTable.deleteById(firstOrder.id)

        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).doesNotContain(firstOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteStockOrderWithThisIdInTheDatabase_ifStockOrderNotExists() = runTest {
        stockOrderTable.insert(firstOrder)
        stockOrderTable.insert(secondOrder)

        stockOrderTable.deleteById(thirdOrder.id)

        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).containsExactly(firstOrder, secondOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockOrderInTheDatabase_ifDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstOrder)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).containsExactly(firstOrder, secondOrder, thirdItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            stockOrderTable.getAll().collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItemsNotPaid_ifDatabaseNotEmpty() = runTest {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondItem)
        stockOrderTable.insert(thirdOrder)

        launch(Dispatchers.IO) {
            stockOrderTable.getDebitStock().collect {
                assertThat(it).containsExactly(firstItem, secondItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoDebitInDatabase() = runTest {
        stockOrderTable.insert(firstOrder)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdItem)

        launch(Dispatchers.IO) {
            stockOrderTable.getDebitStock().collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllItemsOutOfStock_ifDatabaseNotEmpty() = runTest {
        val item1 = makeRandomStockOrder(id = 1L, product = product1, quantity = 10, isOrderedByCustomer = false)
        val item2 = makeRandomStockOrder(id = 2L, product = product2, quantity = 0, isOrderedByCustomer = false)
        val item3 = makeRandomStockOrder(id = 3L, product = product3, quantity = 0, isOrderedByCustomer = false)
        stockOrderTable.insert(item1)
        stockOrderTable.insert(item2)
        stockOrderTable.insert(item3)

        launch(Dispatchers.IO) {
            stockOrderTable.getOutOfStock().collect {
                assertThat(it).containsExactly(item2, item3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoItemsOutOfStockInDatabase() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getOutOfStock().collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllStockItems_ifDatabaseIsNotEmpty() = runTest {
        val item1 = makeRandomStockOrder(id = 1L, product = product1, quantity = 10, isOrderedByCustomer = false)
        val item2 = makeRandomStockOrder(id = 2L, product = product2, quantity = 0, isOrderedByCustomer = false)
        val item3 = makeRandomStockOrder(id = 3L, product = product3, quantity = 12, isOrderedByCustomer = false)
        stockOrderTable.insert(item1)
        stockOrderTable.insert(item2)
        stockOrderTable.insert(item3)

        launch(Dispatchers.IO) {
            stockOrderTable.getStockOrderItems(isOrderedByCustomer = false).collect {
                assertThat(it).containsExactly(item1, item3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllOrders_ifDatabaseIsNotEmpty() = runTest {
        val order1 = makeRandomStockOrder(id = 1L, product = product1, quantity = 10, isOrderedByCustomer = true)
        val order2 = makeRandomStockOrder(id = 2L, product = product2, quantity = 0, isOrderedByCustomer =  true)
        val order3 = makeRandomStockOrder(id = 3L, product = product3, quantity = 12, isOrderedByCustomer = true)
        stockOrderTable.insert(order1)
        stockOrderTable.insert(order2)
        stockOrderTable.insert(order3)

        launch(Dispatchers.IO) {
            stockOrderTable.getStockOrderItems(isOrderedByCustomer = true).collect {
                assertThat(it).containsExactly(order1, order3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoStockOrderInDatabase() = runTest {
        launch(Dispatchers.IO) {
            stockOrderTable.getStockOrderItems(isOrderedByCustomer = true).collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllDebitItemOrderedByPriceDesc_ifNotEmpty() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getDebitStockByPrice(isOrderedAsc = false).collect {
                assertThat(it).containsExactly(secondItem, firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllDebitItemOrderedByPriceAsc_ifNotEmpty() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getDebitStockByPrice(isOrderedAsc = true).collect {
                assertThat(it).containsExactly(firstItem, secondItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllDebitItemOrderedByNameDesc_ifNotEmpty() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getDebitStockByName(isOrderedAsc = false).collect {
                assertThat(it).containsExactly(firstItem, secondItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllDebitItemOrderedByNameAsc_ifNotEmpty() = runTest {
        insertAllItems()
        launch(Dispatchers.IO) {
            stockOrderTable.getDebitStockByName(isOrderedAsc = true).collect {
                assertThat(it).containsExactly(secondItem, firstItem)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnStockOrderWithThisId() = runTest {
        insertAllOrders()
        launch(Dispatchers.IO) {
            stockOrderTable.getById(id = secondOrder.id).collect {
                assertThat(it).isEqualTo(secondOrder)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastItemInsertedInDatabase() = runTest {
        insertAllOrders()
        launch(Dispatchers.IO) {
            stockOrderTable.getLast().collect {
                assertThat(it).isEqualTo(thirdOrder)
                cancel()
            }
        }
    }

    private suspend fun insertAllItems() {
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondItem)
        stockOrderTable.insert(thirdItem)
    }

    private suspend fun insertAllOrders() {
        stockOrderTable.insert(firstOrder)
        stockOrderTable.insert(secondOrder)
        stockOrderTable.insert(thirdOrder)
    }
}