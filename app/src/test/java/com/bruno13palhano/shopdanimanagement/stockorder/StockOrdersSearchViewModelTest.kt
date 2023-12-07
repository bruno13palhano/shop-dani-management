package com.bruno13palhano.shopdanimanagement.stockorder

import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSearchCache
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestSearchCacheRepository
import com.bruno13palhano.shopdanimanagement.repository.TestStockOrderRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.StockOrdersSearchViewModel
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
class StockOrdersSearchViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var stockOrderRepository: StockOrderRepository<StockOrder>
    private lateinit var searchCacheRepository: SearchCacheRepository<SearchCache>
    private lateinit var sut: StockOrdersSearchViewModel

    @Before
    fun setup() {
        stockOrderRepository = TestStockOrderRepository()
        searchCacheRepository = TestSearchCacheRepository()
        sut = StockOrdersSearchViewModel(stockOrderRepository, searchCacheRepository)
    }

    @Test
    fun insertSearch_shouldCallInsertFromSearchCacheRepository() = runTest {
        val search = "test"
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val searchCacheRepository = mock<SearchCacheRepository<SearchCache>>()
        val sut = StockOrdersSearchViewModel(stockOrderRepository, searchCacheRepository)

        sut.insertSearch(search = search)
        advanceUntilIdle()

        verify(searchCacheRepository).insert(any())
    }

    @Test
    fun getSearchCache_shouldSetSearchCacheProperty() = runTest {
        val cache = listOf(makeRandomSearchCache(), makeRandomSearchCache(), makeRandomSearchCache())
        cache.forEach { searchCacheRepository.insert(it) }

        val collectJob = launch { sut.searchCache.collect() }
        sut.getSearchCache()
        advanceUntilIdle()

        assertEquals(cache, sut.searchCache.value)

        collectJob.cancel()
    }

    @Test
    fun getSearchCache_shouldCallGetAllFromSearchCacheRepository() = runTest {
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val searchCacheRepository = mock<SearchCacheRepository<SearchCache>>()
        val sut = StockOrdersSearchViewModel(stockOrderRepository, searchCacheRepository)

        whenever(searchCacheRepository.getAll()).doAnswer { flowOf() }

        sut.getSearchCache()
        advanceUntilIdle()

        verify(searchCacheRepository).getAll()
    }

    @Test
    fun search_shouldSetStockOrderItemsProperty() = runTest {
        val items = listOf(
            makeRandomStockOrder(id = 1L, isOrderedByCustomer = true),
            makeRandomStockOrder(id = 2L, isOrderedByCustomer = false),
            makeRandomStockOrder(id = 3L, isOrderedByCustomer = false)
        )
        val item = items[1]
        val search = item.name
        items.forEach { stockOrderRepository.insert(it) }

        val collectJob = launch { sut.stockOrderItems.collect() }
        sut.search(search = search, isOrderedByCustomer = false)
        advanceUntilIdle()

        assertEquals(mapToStock(listOf(item)), sut.stockOrderItems.value)

        collectJob.cancel()
    }

    @Test
    fun search_shouldCallSearchFromStockOrderRepository() = runTest {
        val search = "Perfumes"
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val searchCacheRepository = mock<SearchCacheRepository<SearchCache>>()
        val sut = StockOrdersSearchViewModel(stockOrderRepository, searchCacheRepository)

        whenever(stockOrderRepository.search(any(), any())).doAnswer { flowOf() }

        sut.search(search = search, isOrderedByCustomer = true)
        advanceUntilIdle()

        verify(stockOrderRepository).search(any(), any())
    }

    private fun mapToStock(items: List<StockOrder>) =
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