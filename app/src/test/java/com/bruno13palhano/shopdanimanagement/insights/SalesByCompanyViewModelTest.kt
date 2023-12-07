package com.bruno13palhano.shopdanimanagement.insights

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.SalesByCompanyViewModel
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
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SalesByCompanyViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleRepository<Sale>
    private lateinit var sut: SalesByCompanyViewModel

    private val sales = listOf(
        makeRandomSale(
            id = 1L,
            stockOrder = makeRandomStockOrder(id = 1L, isOrderedByCustomer = true),
            canceled = false
        ),
        makeRandomSale(
            id = 2L,
            stockOrder = makeRandomStockOrder(id = 2L, isOrderedByCustomer = true),
            canceled = false
        ),
        makeRandomSale(
            id = 3L,
            stockOrder = makeRandomStockOrder(id = 3L, isOrderedByCustomer = false),
            canceled = false
        ),
        makeRandomSale(
            id = 4L,
            stockOrder = makeRandomStockOrder(id = 4L, isOrderedByCustomer = false),
            canceled = false
        )
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = SalesByCompanyViewModel(saleRepository)
    }

    @Test
    fun getChartByRange_shouldCallGetAllFromRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = SalesByCompanyViewModel(saleRepository)

        whenever(saleRepository.getAll()).doAnswer { flowOf() }

        sut.getChartByRange(rangeOfDays = 7)
        advanceUntilIdle()

        verify(saleRepository).getAll()
    }

    @Test
    fun getChartByRange_shouldSetChartEntryProperty() = runTest {
        insertSales()

        val collectJob = launch { sut.chartEntry.collect() }

        sut.getChartByRange(7)
        advanceUntilIdle()

        assertEquals(mapToEntries(sales), sut.chartEntry.value)

        collectJob.cancel()
    }

    private suspend fun insertSales() = sales.forEach { saleRepository.insert(it) }

    private fun mapToEntries(sales: List<Sale>): SalesByCompanyViewModel.SalesCompanyEntries {
        val avonEntries = mutableListOf<Pair<String, Float>>()
        val naturaEntries = mutableListOf<Pair<String, Float>>()
        var days = Array(7) { 0 }

        sales.filter { sale -> sale.company == Company.AVON.company }
            .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
        setChartEntries(avonEntries, days)

        days = Array(7) { 0 }
        sales.filter { sale -> sale.company == Company.NATURA.company }
            .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
        setChartEntries(naturaEntries, days)

        return SalesByCompanyViewModel.SalesCompanyEntries(
            avonEntries = avonEntries,
            naturaEntries = naturaEntries
        )
    }
}