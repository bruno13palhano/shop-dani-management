package com.bruno13palhano.shopdanimanagement.financial

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomDelivery
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestDeliveryRepository
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.FinancialInfoViewModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
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

    private lateinit var saleRepository: SaleData<Sale>
    private lateinit var deliveryRepository: DeliveryData<Delivery>
    private lateinit var sut: FinancialInfoViewModel

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
    private val deliveries = listOf(
        makeRandomDelivery(id = 1L, saleId = 1L),
        makeRandomDelivery(id = 2L, saleId = 2L),
        makeRandomDelivery(id = 3L, saleId = 3L)
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        deliveryRepository = TestDeliveryRepository()
        sut = FinancialInfoViewModel(saleRepository, deliveryRepository)
    }

    @Test
    fun financial_shouldSetSalesAndDeliveriesValues() = runTest {
        insertSales()
        insertDeliveries()

        val collectJob = launch { sut.financial.collect() }
        advanceUntilIdle()

        assertEquals(mapToInfo(sales, deliveries), sut.financial.value)

        collectJob.cancel()
    }

    @Test
    fun entry_shouldSetChartProperties() = runTest {
        insertSales()
        insertDeliveries()

        val collectJob = launch { sut.entry.collect() }
        advanceUntilIdle()

        val info = mapToInfo(sales, deliveries)
        val expected = ChartEntryModelProducer(
            listOf(
                listOf(FloatEntry(0F, info.allSales)),
                listOf(FloatEntry(0F, info.stockSales)),
                listOf(FloatEntry(0F, info.ordersSales)),
                listOf(FloatEntry(0F, info.profit))
            )
        ).getModel().entries

        assertEquals(expected, sut.entry.value.getModel().entries)

        collectJob.cancel()
    }

    private fun mapToInfo(
        sales: List<Sale>,
        deliveries: List<Delivery>
    ): FinancialInfoViewModel.FinancialInfo {
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

        deliveries.map {
            allDeliveriesPrice += it.deliveryPrice
        }

        return FinancialInfoViewModel.FinancialInfo(
            allSales = allSales,
            stockSales = stockSales,
            ordersSales = ordersSales,
            profit = allSales - (allSalesPurchasePrice + allDeliveriesPrice),
        )
    }

    private suspend fun insertSales() = sales.forEach { saleRepository.insert(it) }
    private suspend fun insertDeliveries() = deliveries.forEach { deliveryRepository.insert(it) }
}