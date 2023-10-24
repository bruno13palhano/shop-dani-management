package com.bruno13palhano.shopdanimanagement.insights

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.ChartsViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.setChartEntries
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
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
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ChartsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private val currentDay = LocalDate.now()
    private lateinit var saleRepository: SaleData<Sale>
    private lateinit var sut: ChartsViewModel

    private val sales = listOf(
        makeRandomSale(
            id = 1L,
            dateOfSale = currentDay.minusDays(2).atStartOfDay()
                .toInstant(ZoneOffset.UTC).toEpochMilli(),
            canceled = false
        ),
        makeRandomSale(
            id = 2L,
            dateOfSale = currentDay.minusDays(1).atStartOfDay()
                .toInstant(ZoneOffset.UTC).toEpochMilli(),
            canceled = false
        ),
        makeRandomSale(
            id = 3L,
            dateOfSale = currentDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            canceled = false
        )
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = ChartsViewModel(saleRepository)
    }

    @Test
    fun setItemsDayRange_shouldCallGetLastSalesFromRepository() = runTest {
        val saleRepository = mock<SaleData<Sale>>()
        val sut = ChartsViewModel(saleRepository)

        whenever(saleRepository.getLastSales(any(), any())).doAnswer { flowOf() }

        sut.setItemsDaysRange(rangeOfDays = 7)
        advanceUntilIdle()

        verify(saleRepository).getLastSales(any(), any())
    }

    @Test
    fun setItemsDayRange_shouldSetLastSalesEntriesProperty() = runTest {
        insertSales()

        val collectJob = launch { sut.lastSalesEntries.collect() }

        sut.setItemsDaysRange(rangeOfDays = 7)
        advanceUntilIdle()

        assertEquals(mapToEntries(sales), sut.lastSalesEntries.value)

        collectJob.cancel()
    }

    private suspend fun insertSales() = sales.forEach { saleRepository.insert(it) }

    private fun mapToEntries(sales: List<Sale>): ChartsViewModel.SalesEntries {
        val stockEntries = mutableListOf<Pair<String, Float>>()
        val ordersEntries = mutableListOf<Pair<String, Float>>()
        val lastSalesEntries = mutableListOf<Pair<String, Float>>()
        var days = Array(7) { 0 }

        sales.filter { sale -> !sale.isOrderedByCustomer }
            .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
        setChartEntries(stockEntries, days)

        sales.filter { sale -> sale.isOrderedByCustomer }
            .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
        setChartEntries(ordersEntries, days)

        days = Array(7) { 0 }

        sales.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
        setChartEntries(lastSalesEntries, days)

        return ChartsViewModel.SalesEntries(
            lastSalesEntries = lastSalesEntries,
            stockEntries = stockEntries,
            orderEntries = ordersEntries
        )
    }
}