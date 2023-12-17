package com.bruno13palhano.shopdanimanagement.financial

import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomStockItem
import com.bruno13palhano.shopdanimanagement.repository.TestStockRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.StockDebitsViewModel
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
class StockDebitsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var stockRepository: StockRepository
    private lateinit var sut: StockDebitsViewModel

    private val debits = listOf(
        makeRandomStockItem(
            id = 1L,
            product = makeRandomProduct(id = 1L, name = "A"),
            purchasePrice = 25F,
            isPaid = false
        ),
        makeRandomStockItem(id = 2L,
            purchasePrice = 50F,
            product = makeRandomProduct(id = 2L, name = "B"),
            isPaid = false
        ),
        makeRandomStockItem(
            id = 3L,
            product = makeRandomProduct(id = 3L, name = "C"),
            purchasePrice = 75F,
            isPaid = true
        )
    )

    @Before
    fun setup() {
        stockRepository = TestStockRepository()
        sut = StockDebitsViewModel(stockRepository)
    }

    @Test
    fun getDebitStock_shouldCallGetDebitStockFromRepository() = runTest {
        val stockRepository = mock<StockRepository>()
        val sut = StockDebitsViewModel(stockRepository)

        whenever(stockRepository.getDebitStock()).doAnswer { flowOf() }

        sut.getDebitStock()
        advanceUntilIdle()

        verify(stockRepository).getDebitStock()
    }

    @Test
    fun getDebitStock_shouldSetDebitItemsProperty() = runTest {
        insertDebits()
        val expected = mapToStock(listOf(debits[0], debits[1]))

        val collectJob = launch { sut.debitItems.collect() }

        sut.getDebitStock()
        advanceUntilIdle()

        assertEquals(expected, sut.debitItems.value)

        collectJob.cancel()
    }

    @Test
    fun getStockByPrice_shouldCallGetDebitStockByPriceFromRepository() = runTest {
        val stockRepository = mock<StockRepository>()
        val sut = StockDebitsViewModel(stockRepository)

        whenever(stockRepository.getDebitStockByPrice(any())).doAnswer { flowOf() }

        sut.getStockByPrice(isOrderedAsc = true)
        advanceUntilIdle()

        verify(stockRepository).getDebitStockByPrice(any())
    }

    @Test
    fun getStockByPrice_shouldSetDebitItemsProperty() = runTest {
        insertDebits()
        val expected = mapToStock(listOf(debits[1], debits[0]))

        val collectJob = launch { sut.debitItems.collect() }

        sut.getStockByPrice(isOrderedAsc = false)
        advanceUntilIdle()

        assertEquals(expected, sut.debitItems.value)

        collectJob.cancel()
    }

    @Test
    fun getStockByName_shouldCallGetStockByNameFromRepository() = runTest {
        val stockRepository = mock<StockRepository>()
        val sut = StockDebitsViewModel(stockRepository)

        whenever(stockRepository.getDebitStockByName(any())).doAnswer { flowOf() }

        sut.getStockByName(isOrderedAsc = true)
        advanceUntilIdle()

        verify(stockRepository).getDebitStockByName(any())
    }

    @Test
    fun getStockByName_shouldSetDebitItemsProperty() = runTest {
        insertDebits()
        val expected = mapToStock(listOf(debits[0], debits[1]))

        val collectJob = launch { sut.debitItems.collect() }

        sut.getStockByName(isOrderedAsc = true)
        advanceUntilIdle()

        assertEquals(expected, sut.debitItems.value)

        collectJob.cancel()
    }

    private fun mapToStock(debits: List<StockItem>) = debits.map {
        Stock(
            id = it.id,
            name = it.name,
            photo = it.photo,
            purchasePrice = it.purchasePrice,
            quantity = it.quantity
        )
    }

    private suspend fun insertDebits() = debits.forEach { stockRepository.insert(it, {}, {}) }
}