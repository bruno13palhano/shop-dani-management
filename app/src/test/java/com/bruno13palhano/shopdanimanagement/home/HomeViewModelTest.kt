package com.bruno13palhano.shopdanimanagement.home

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleData<Sale>
    private lateinit var sut: HomeViewModel

    private val currentDay = LocalDate.now()

    private val sales = listOf(
        makeRandomSale(
            id = 1L,
            stockOrder = makeRandomStockOrder(
                id = 1,
                isOrderedByCustomer = false,
                salePrice = 25F
            ),
            quantity = 1,
            canceled = false
        ),
        makeRandomSale(
            id = 2L,
            stockOrder = makeRandomStockOrder(
                id = 2,
                isOrderedByCustomer = false,
                salePrice = 50F
            ),
            quantity = 1,
            canceled = false
        ),
        makeRandomSale(
            id = 3L,
            stockOrder = makeRandomStockOrder(
                id = 3,
                isOrderedByCustomer = true,
                salePrice = 75F
            ),
            quantity = 1,
            canceled = false
        )
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = HomeViewModel(saleRepository)
    }

    @Test
    fun homeInfo_shouldSetSaleValues() = runTest {
        insertSales()

        val collectJob = launch { sut.homeInfo.collect() }
        advanceUntilIdle()

        assertEquals(mapToInfo(sales), sut.homeInfo.value)

        collectJob.cancel()
    }

    @Test
    fun lastSales_shouldGetLastsSales() = runTest {
        insertSales()

        val collectJob = launch { sut.lastSales.collect() }
        advanceUntilIdle()

        assertEquals(mapToChart(sales), sut.lastSales.value)

        collectJob.cancel()
    }

    private suspend fun insertSales() = sales.forEach { saleRepository.insert(it) }

    private fun mapToInfo(sales: List<Sale>): HomeViewModel.HomeInfo {
        var salesPrice = 0F
        var purchasePrice = 0F
        var biggestSale = HomeViewModel.Info()
        var biggestSaleValue = 0F
        var smallestSale = HomeViewModel.Info()
        var smallestSaleValue = Float.MAX_VALUE
        var lastSale = HomeViewModel.Info()

        sales.map { sale ->
            salesPrice += sale.salePrice
            purchasePrice += sale.purchasePrice
            if (biggestSaleValue <= sale.salePrice) {
                biggestSaleValue = sale.salePrice
                biggestSale = HomeViewModel.Info(
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = sale.dateOfSale
                )
            }
            if (smallestSaleValue >= sale.salePrice) {
                smallestSaleValue = sale.salePrice
                smallestSale = HomeViewModel.Info(
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = sale.dateOfSale
                )
            }
            if (sales.lastIndex != -1) {
                val last = sales.last()
                lastSale = HomeViewModel.Info(
                    value = (last.quantity * last.salePrice),
                    customer = last.customerName,
                    item = last.name,
                    quantity = last.quantity,
                    date = last.dateOfSale
                )
            }
        }
        return HomeViewModel.HomeInfo(
            sales = salesPrice,
            profit = salesPrice - purchasePrice,
            biggestSale = biggestSale,
            smallestSale = smallestSale,
            lastSale = lastSale
        )
    }

    private fun mapToChart(sales: List<Sale>): List<Pair<String, Float>> {
        val days = arrayOf(0, 0, 0, 0, 0, 0, 0)
        val chart = mutableListOf<Pair<String, Float>>()

        sales.map { setDay(days, it.dateOfSale, it.quantity) }
        setChartEntries(chart, days)

        return chart
    }

    private fun setDay(days: Array<Int>, date: Long, quantity: Int) {
        for (i in days.indices) {
            if (LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("UTC")).toLocalDate()
                == currentDay.minusDays(i.toLong())) {
                days[i] += quantity
            }
        }
    }

    private fun setChartEntries(chart: MutableList<Pair<String, Float>>, days: Array<Int>) {
        for (i in days.size-1 downTo 0) {
            chart.add(Pair(currentDay.minusDays(i.toLong()).dayOfWeek.name, days[i].toFloat()))
        }
    }
}