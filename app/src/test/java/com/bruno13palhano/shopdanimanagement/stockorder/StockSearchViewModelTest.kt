package com.bruno13palhano.shopdanimanagement.stockorder

import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSearchCache
import com.bruno13palhano.shopdanimanagement.makeRandomStockItem
import com.bruno13palhano.shopdanimanagement.repository.TestSearchCacheRepository
import com.bruno13palhano.shopdanimanagement.repository.TestStockRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.StockSearchViewModel
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
class StockSearchViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var stockItemRepository: StockRepository
    private lateinit var searchCacheRepository: SearchCacheRepository
    private lateinit var sut: StockSearchViewModel

    @Before
    fun setup() {
        stockItemRepository = TestStockRepository()
        searchCacheRepository = TestSearchCacheRepository()
        sut = StockSearchViewModel(stockItemRepository, searchCacheRepository)
    }

    @Test
    fun insertSearch_shouldCallInsertFromSearchCacheRepository() =
        runTest {
            val search = "test"
            val stockItemRepository = mock<StockRepository>()
            val searchCacheRepository = mock<SearchCacheRepository>()
            val sut = StockSearchViewModel(stockItemRepository, searchCacheRepository)

            sut.insertSearch(search = search)
            advanceUntilIdle()

            verify(searchCacheRepository).insert(any())
        }

    @Test
    fun getSearchCache_shouldSetSearchCacheProperty() =
        runTest {
            val cache = listOf(makeRandomSearchCache(), makeRandomSearchCache(), makeRandomSearchCache())
            cache.forEach { searchCacheRepository.insert(it) }

            val collectJob = launch { sut.searchCache.collect() }
            sut.getSearchCache()
            advanceUntilIdle()

            assertEquals(cache, sut.searchCache.value)

            collectJob.cancel()
        }

    @Test
    fun getSearchCache_shouldCallGetAllFromSearchCacheRepository() =
        runTest {
            val stockItemRepository = mock<StockRepository>()
            val searchCacheRepository = mock<SearchCacheRepository>()
            val sut = StockSearchViewModel(stockItemRepository, searchCacheRepository)

            whenever(searchCacheRepository.getAll()).doAnswer { flowOf() }

            sut.getSearchCache()
            advanceUntilIdle()

            verify(searchCacheRepository).getAll()
        }

    @Test
    fun search_shouldSetStockOrderItemsProperty() =
        runTest {
            val items =
                listOf(
                    makeRandomStockItem(id = 1L),
                    makeRandomStockItem(id = 2L),
                    makeRandomStockItem(id = 3L)
                )
            val item = items[1]
            val search = item.name
            items.forEach { stockItemRepository.insert(it, {}, {}) }

            val collectJob = launch { sut.stockOrderItems.collect() }
            sut.search(search = search)
            advanceUntilIdle()

            assertEquals(mapToStock(listOf(item)), sut.stockOrderItems.value)

            collectJob.cancel()
        }

    @Test
    fun search_shouldCallSearchFromStockOrderRepository() =
        runTest {
            val search = "Perfumes"
            val stockItemRepository = mock<StockRepository>()
            val searchCacheRepository = mock<SearchCacheRepository>()
            val sut = StockSearchViewModel(stockItemRepository, searchCacheRepository)

            whenever(stockItemRepository.search(any())).doAnswer { flowOf() }

            sut.search(search = search)
            advanceUntilIdle()

            verify(stockItemRepository).search(any())
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