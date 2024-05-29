package com.bruno13palhano.shopdanimanagement.sales

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockItem
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesViewModel
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
class SalesViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var salesRepository: SaleRepository
    private lateinit var sut: SalesViewModel

    private val sales =
        listOf(
            makeRandomSale(
                id = 1L,
                stockItem = makeRandomStockItem(id = 1L, salePrice = 50F),
                customer = makeRandomCustomer(id = 1L, name = "Alex"),
                canceled = false
            ),
            makeRandomSale(
                id = 2L,
                stockItem = makeRandomStockItem(id = 2L, salePrice = 100F),
                customer = makeRandomCustomer(id = 2L, name = "Bruno"),
                canceled = true
            ),
            makeRandomSale(
                id = 3L,
                stockItem = makeRandomStockItem(id = 3L, salePrice = 125F),
                customer = makeRandomCustomer(id = 3L, "Carlos"),
                canceled = false
            )
        )

    @Before
    fun setup() {
        salesRepository = TestSaleRepository()
        sut = SalesViewModel(salesRepository)
    }

    @Test
    fun getSales_shouldCallGetAllFromRepository() =
        runTest {
            val salesRepository = mock<SaleRepository>()
            val sut = SalesViewModel(salesRepository)

            whenever(salesRepository.getAll()).doAnswer { flowOf() }

            sut.getSales()
            advanceUntilIdle()

            verify(salesRepository).getAll()
        }

    @Test
    fun getSales_shouldSetSaleListProperty() =
        runTest {
            val expectedSales = listOf(sales[0], sales[2])
            sales.forEach { salesRepository.insert(it, {}, {}) }

            val collectJob = launch { sut.saleList.collect() }
            sut.getSales()
            advanceUntilIdle()

            assertEquals(mapToItem(expectedSales), sut.saleList.value)

            collectJob.cancel()
        }

    @Test
    fun getSalesByCustomerName_shouldCallGetAllSalesByCustomerNameFromRepository() =
        runTest {
            val salesRepository = mock<SaleRepository>()
            val sut = SalesViewModel(salesRepository)

            whenever(salesRepository.getAllSalesByCustomerName(any())).doAnswer { flowOf() }

            sut.getSalesByCustomerName(isOrderedAsc = true)
            advanceUntilIdle()

            verify(salesRepository).getAllSalesByCustomerName(any())
        }

    @Test
    fun getSalesByCustomerName_shouldSetSaleListProperty() =
        runTest {
            val expectedSales = listOf(sales[2], sales[0])
            sales.forEach { salesRepository.insert(it, {}, {}) }

            val collectJob = launch { sut.saleList.collect() }
            sut.getSalesByCustomerName(isOrderedAsc = false)
            advanceUntilIdle()

            assertEquals(mapToItem(expectedSales), sut.saleList.value)

            collectJob.cancel()
        }

    @Test
    fun getSalesBySalePrice_shouldCallGetAllSalesBySalePriceFromRepository() =
        runTest {
            val salesRepository = mock<SaleRepository>()
            val sut = SalesViewModel(salesRepository)

            whenever(salesRepository.getAllSalesBySalePrice(any())).doAnswer { flowOf() }

            sut.getSalesBySalePrice(isOrderedAsc = true)
            advanceUntilIdle()

            verify(salesRepository).getAllSalesBySalePrice(any())
        }

    @Test
    fun getSalesBySalePrice_shouldSetSaleListProperty() =
        runTest {
            val expectedSales = listOf(sales[2], sales[0])
            sales.forEach { salesRepository.insert(it, {}, {}) }

            val collectJob = launch { sut.saleList.collect() }
            sut.getSalesBySalePrice(isOrderedAsc = false)
            advanceUntilIdle()

            assertEquals(mapToItem(expectedSales), sut.saleList.value)

            collectJob.cancel()
        }

    private fun mapToItem(sales: List<Sale>) =
        sales.map {
            CommonItem(
                id = it.id,
                photo = it.photo,
                title = it.name,
                subtitle = it.company,
                description = it.dateOfSale.toString()
            )
        }
}