package com.bruno13palhano.shopdanimanagement.financial

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockItem
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.FinancialInfoViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FinancialInfoViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleRepository
    private lateinit var sut: FinancialInfoViewModel

    private val sales = listOf(
        makeRandomSale(
            id = 1L,
            stockItem = makeRandomStockItem(
                id = 1,
                salePrice = 25F
            ),
            quantity = 1,
            canceled = false
        ),
        makeRandomSale(
            id = 2L,
            stockItem = makeRandomStockItem(
                id = 2,
                salePrice = 50F
            ),
            quantity = 1,
            canceled = false
        ),
        makeRandomSale(
            id = 3L,
            stockItem = makeRandomStockItem(
                id = 3,
                salePrice = 75F
            ),
            isOrderedByCustomer = true,
            quantity = 1,
            canceled = false
        )
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = FinancialInfoViewModel(saleRepository)
    }

    @Test
    fun financial_shouldSetSalesAndDeliveriesValues() = runTest {
        insertSales()

        val collectJob = launch { sut.financial.collect() }
        advanceUntilIdle()

        assertEquals(mapToInfo(sales), sut.financial.value)

        collectJob.cancel()
    }

    @Test
    fun entry_shouldSetChartProperties() = runTest {
        insertSales()

        val collectJob = launch { sut.entry.collect() }
        advanceUntilIdle()

        val info = mapToInfo(sales)
        val expected = FinancialInfoViewModel.FinancialChartEntries(
            allSalesEntries = Pair(0F, info.allSales),
            stockSalesEntries = Pair(0F, info.stockSales),
            ordersSalesEntries = Pair(0F, info.ordersSales),
            profitEntries = Pair(0F, info.profit)
        )
        assertEquals(expected, sut.entry.value)

        collectJob.cancel()
    }

    private fun mapToInfo(sales: List<Sale>): FinancialInfoViewModel.FinancialInfo {
        var allSalesPurchasePrice = 0F
        var allSales = 0F
        var allDeliveriesPrice = 0F
        var stockSales = 0F
        var ordersSales = 0F

        sales.map {
            allSales += it.salePrice
            allSalesPurchasePrice += it.purchasePrice

            if (it.isOrderedByCustomer) {
                ordersSales += it.salePrice
            } else {
                stockSales += it.salePrice
            }
        }

        sales.map {
            allDeliveriesPrice += it.deliveryPrice
        }

        return FinancialInfoViewModel.FinancialInfo(
            allSales = allSales,
            stockSales = stockSales,
            ordersSales = ordersSales,
            profit = allSales - (allSalesPurchasePrice + allDeliveriesPrice),
        )
    }

    private suspend fun insertSales() = sales.forEach { saleRepository.insert(it, {}, {}) }
}