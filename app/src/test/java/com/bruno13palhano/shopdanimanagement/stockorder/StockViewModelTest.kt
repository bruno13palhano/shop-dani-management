package com.bruno13palhano.shopdanimanagement.stockorder

import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.stockorder.StockRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCategory
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestCategoryRepository
import com.bruno13palhano.shopdanimanagement.repository.TestStockRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.StockOrdersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StockOrdersViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var stockItemRepository: StockRepository<StockItem>
    private lateinit var categoryRepository: CategoryRepository<Category>
    private lateinit var sut: StockOrdersViewModel

    @Before
    fun setup() {
        stockItemRepository = TestStockRepository()
        categoryRepository = TestCategoryRepository()
        sut = StockOrdersViewModel(stockItemRepository, categoryRepository)
    }

    @Test
    fun categories_shouldReturnAllCategories() = runTest {
        val categories = listOf(makeRandomCategory(id = 1L), makeRandomCategory(id = 2L))
        categories.forEach { categoryRepository.insert(it) }

        val collectJob = launch { sut.categories.collect() }
        advanceUntilIdle()

        assertEquals(categories.map { it.category }, sut.categories.value)

        collectJob.cancel()
    }

    @Test
    fun getItems_shouldCallGetStockOrderItemsFromStockOrderRepository() = runTest {
        val stockItemRepository = mock<StockRepository<StockItem>>()
        val categoryRepository = mock<CategoryRepository<Category>>()
        val sut = StockOrdersViewModel(stockItemRepository, categoryRepository)

        whenever(stockItemRepository.getStockOrderItems(any())).doAnswer { flowOf() }

        sut.getItems(isOrderedByCustomer = true)
        advanceUntilIdle()

        verify(stockItemRepository).getStockOrderItems(any())
    }

    @Test
    fun getItems_shouldSetStockList() = runTest {
        val items = listOf(
            makeRandomStockOrder(id = 1L, quantity = 0),
            makeRandomStockOrder(id = 2L, isOrderedByCustomer = true),
            makeRandomStockOrder(id = 3L, isOrderedByCustomer = false)
        )
        items.forEach { stockItemRepository.insert(it) }
        val expectedItem = items[1]

        val collectJob = launch { sut.stockList.collect() }

        sut.getItems(isOrderedByCustomer = true)
        advanceUntilIdle()

        assertEquals(mapToStock(listOf(expectedItem)), sut.stockList.value)

        collectJob.cancel()
    }

    @Test
    fun getItemsByCategories_shouldCallGetByCategoryFromStockOrderRepository() = runTest {
        val stockItemRepository = mock<StockRepository<StockItem>>()
        val categoryRepository = mock<CategoryRepository<Category>>()
        val sut = StockOrdersViewModel(stockItemRepository, categoryRepository)

        whenever(stockItemRepository.getByCategory(any(), any())).doAnswer { flowOf() }

        sut.getItemsByCategories(category = "Perfumes", isOrderedByCustomer = true)
        advanceUntilIdle()

        verify(stockItemRepository).getByCategory(any(), any())
    }

    @Test
    fun getItemsByCategory_shouldSetStockList() = runTest {
        val items = listOf(
            makeRandomStockOrder(id = 1L, quantity = 0),
            makeRandomStockOrder(id = 2L, isOrderedByCustomer = true),
            makeRandomStockOrder(id = 3L, isOrderedByCustomer = false)
        )
        items.forEach { stockItemRepository.insert(it) }
        val expectedItem = items[2]
        val category = expectedItem.categories[0].category

        val collectJob = launch { sut.stockList.collect() }

        sut.getItemsByCategories(category = category, isOrderedByCustomer = false)
        advanceUntilIdle()

        assertEquals(mapToStock(listOf(expectedItem)), sut.stockList.value)

        collectJob.cancel()
    }

    @Test
    fun getOutOfStock_shouldCallGetOutOfStockFromStockOrderRepository() = runTest {
        val stockItemRepository = mock<StockRepository<StockItem>>()
        val categoryRepository = mock<CategoryRepository<Category>>()
        val sut = StockOrdersViewModel(stockItemRepository, categoryRepository)

        whenever(stockItemRepository.getOutOfStock()).doAnswer { flowOf() }

        sut.getOutOfStock()
        advanceUntilIdle()

        verify(stockItemRepository).getOutOfStock()
    }

    @Test
    fun getOutOfStock_shouldSetStockList() = runTest {
        val items = listOf(
            makeRandomStockOrder(id = 1L, isOrderedByCustomer = false, quantity = 0),
            makeRandomStockOrder(id = 2L, isOrderedByCustomer = true),
            makeRandomStockOrder(id = 3L, isOrderedByCustomer = false)
        )
        items.forEach { stockItemRepository.insert(it) }
        val expectedItem = items[0]

        val collectJob = launch { sut.stockList.collect() }

        sut.getOutOfStock()
        advanceUntilIdle()

        assertEquals(mapToStock(listOf(expectedItem)), sut.stockList.value)

        collectJob.cancel()
    }

    private fun mapToStock(items: List<StockItem>) =
        items.map {
            Stock(
                id = it.id,
                name = it.name,
                photo = it.photo,
                purchasePrice = it.purchasePrice,
                quantity = it.quantity
            )
        }
}