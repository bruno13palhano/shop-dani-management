package com.bruno13palhano.shopdanimanagement.stockorder

import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCategory
import com.bruno13palhano.shopdanimanagement.makeRandomStockItem
import com.bruno13palhano.shopdanimanagement.repository.TestCategoryRepository
import com.bruno13palhano.shopdanimanagement.repository.TestStockRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.StockViewModel
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
class StockViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var stockItemRepository: StockRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var sut: StockViewModel

    @Before
    fun setup() {
        stockItemRepository = TestStockRepository()
        categoryRepository = TestCategoryRepository()
        sut = StockViewModel(stockItemRepository, categoryRepository)
    }

    @Test
    fun categories_shouldReturnAllCategories() = runTest {
        val categories = listOf(makeRandomCategory(id = 1L), makeRandomCategory(id = 2L))
        categories.forEach { categoryRepository.insert(it, {}, {}) }

        val collectJob = launch { sut.categories.collect() }
        advanceUntilIdle()

        assertEquals(categories.map { it.category }, sut.categories.value)

        collectJob.cancel()
    }

    @Test
    fun getItems_shouldCallGetStockOrderItemsFromStockOrderRepository() = runTest {
        val stockItemRepository = mock<StockRepository>()
        val categoryRepository = mock<CategoryRepository>()
        val sut = StockViewModel(stockItemRepository, categoryRepository)

        whenever(stockItemRepository.getStockItems()).doAnswer { flowOf() }

        sut.getItems()
        advanceUntilIdle()

        verify(stockItemRepository).getStockItems()
    }

    @Test
    fun getItems_shouldSetStockList() = runTest {
        val items = listOf(
            makeRandomStockItem(id = 1L, quantity = 0),
            makeRandomStockItem(id = 2L),
            makeRandomStockItem(id = 3L)
        )
        items.forEach { stockItemRepository.insert(it, {}, {}) }
        val expectedItem = listOf(items[1], items[2])

        val collectJob = launch { sut.stockList.collect() }

        sut.getItems()
        advanceUntilIdle()

        assertEquals(mapToStock(expectedItem), sut.stockList.value)

        collectJob.cancel()
    }

    @Test
    fun getItemsByCategories_shouldCallGetByCategoryFromStockOrderRepository() = runTest {
        val stockItemRepository = mock<StockRepository>()
        val categoryRepository = mock<CategoryRepository>()
        val sut = StockViewModel(stockItemRepository, categoryRepository)

        whenever(stockItemRepository.getByCategory(any())).doAnswer { flowOf() }

        sut.getItemsByCategories(category = "Perfumes")
        advanceUntilIdle()

        verify(stockItemRepository).getByCategory(any())
    }

    @Test
    fun getItemsByCategory_shouldSetStockList() = runTest {
        val items = listOf(
            makeRandomStockItem(id = 1L, quantity = 0),
            makeRandomStockItem(id = 2L),
            makeRandomStockItem(id = 3L)
        )
        items.forEach { stockItemRepository.insert(it, {}, {}) }
        val expectedItem = items[2]
        val category = expectedItem.categories[0].category

        val collectJob = launch { sut.stockList.collect() }

        sut.getItemsByCategories(category = category)
        advanceUntilIdle()

        assertEquals(mapToStock(listOf(expectedItem)), sut.stockList.value)

        collectJob.cancel()
    }

    @Test
    fun getOutOfStock_shouldCallGetOutOfStockFromStockOrderRepository() = runTest {
        val stockItemRepository = mock<StockRepository>()
        val categoryRepository = mock<CategoryRepository>()
        val sut = StockViewModel(stockItemRepository, categoryRepository)

        whenever(stockItemRepository.getOutOfStock()).doAnswer { flowOf() }

        sut.getOutOfStock()
        advanceUntilIdle()

        verify(stockItemRepository).getOutOfStock()
    }

    @Test
    fun getOutOfStock_shouldSetStockList() = runTest {
        val items = listOf(
            makeRandomStockItem(id = 1L, quantity = 0),
            makeRandomStockItem(id = 2L),
            makeRandomStockItem(id = 3L)
        )
        items.forEach { stockItemRepository.insert(it, {}, {}) }
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