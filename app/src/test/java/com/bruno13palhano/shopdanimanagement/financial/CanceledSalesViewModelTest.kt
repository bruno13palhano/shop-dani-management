package com.bruno13palhano.shopdanimanagement.financial

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockItem
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CanceledSalesViewModel
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
class CanceledSalesViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleRepository
    private lateinit var sut: CanceledSalesViewModel

    private val canceledSales =
        listOf(
            makeRandomSale(
                id = 1L,
                stockItem =
                    makeRandomStockItem(
                        id = 1L,
                        product = makeRandomProduct(id = 1L, name = "A"),
                        salePrice = 25F
                    ),
                customer = makeRandomCustomer(id = 1L, name = "A"),
                canceled = true
            ),
            makeRandomSale(
                id = 2L,
                stockItem =
                    makeRandomStockItem(
                        id = 2L,
                        product = makeRandomProduct(id = 2L, name = "B"),
                        salePrice = 50F
                    ),
                customer = makeRandomCustomer(id = 1L, name = "B"),
                canceled = true
            ),
            makeRandomSale(id = 3L, canceled = false)
        )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = CanceledSalesViewModel(saleRepository)
    }

    @Test
    fun getAllCanceledSales_shouldCallGetAllCanceledSalesFromRepository() =
        runTest {
            val saleRepository = mock<SaleRepository>()
            val sut = CanceledSalesViewModel(saleRepository)

            whenever(saleRepository.getAllCanceledSales()).doAnswer { flowOf() }

            sut.getAllCanceledSales()
            advanceUntilIdle()

            verify(saleRepository).getAllCanceledSales()
        }

    @Test
    fun getAllCanceledSales_shouldSetCanceledSaleProperty() =
        runTest {
            insertCanceledSales()
            val expected = mapToItem(listOf(canceledSales[0], canceledSales[1]))

            val collectJob = launch { sut.canceledSales.collect() }

            sut.getAllCanceledSales()
            advanceUntilIdle()

            assertEquals(expected, sut.canceledSales.value)

            collectJob.cancel()
        }

    @Test
    fun getAllCanceledSalesByName_shouldCallGetCanceledByNameFromRepository() =
        runTest {
            val saleRepository = mock<SaleRepository>()
            val sut = CanceledSalesViewModel(saleRepository)

            whenever(saleRepository.getCanceledByName(any())).doAnswer { flowOf() }

            sut.getCanceledSalesByName(isOrderedAsc = true)
            advanceUntilIdle()

            verify(saleRepository).getCanceledByName(any())
        }

    @Test
    fun getCanceledSalesByName_shouldSetCanceledSaleProperty() =
        runTest {
            insertCanceledSales()
            val expected = mapToItem(listOf(canceledSales[1], canceledSales[0]))

            val collectJob = launch { sut.canceledSales.collect() }

            sut.getCanceledSalesByName(isOrderedAsc = false)
            advanceUntilIdle()

            assertEquals(expected, sut.canceledSales.value)

            collectJob.cancel()
        }

    @Test
    fun getCanceledSalesByCustomerName_shouldCallGetCanceledByCustomerNameFromRepository() =
        runTest {
            val saleRepository = mock<SaleRepository>()
            val sut = CanceledSalesViewModel(saleRepository)

            whenever(saleRepository.getCanceledByCustomerName(any())).doAnswer { flowOf() }

            sut.getCanceledSalesByCustomerName(isOrderedAsc = true)
            advanceUntilIdle()

            verify(saleRepository).getCanceledByCustomerName(any())
        }

    @Test
    fun getCanceledSalesByCustomerName_shouldSetCanceledSaleProperty() =
        runTest {
            insertCanceledSales()
            val expected = mapToItem(listOf(canceledSales[1], canceledSales[0]))

            val collectJob = launch { sut.canceledSales.collect() }

            sut.getCanceledSalesByCustomerName(isOrderedAsc = false)
            advanceUntilIdle()

            assertEquals(expected, sut.canceledSales.value)

            collectJob.cancel()
        }

    @Test
    fun getCanceledSalesByPrice_shouldCallGetCanceledByPriceFromRepository() =
        runTest {
            val saleRepository = mock<SaleRepository>()
            val sut = CanceledSalesViewModel(saleRepository)

            whenever(saleRepository.getCanceledByPrice(any())).doAnswer { flowOf() }

            sut.getCanceledSalesByPrice(isOrderedAsc = true)
            advanceUntilIdle()

            verify(saleRepository).getCanceledByPrice(any())
        }

    @Test
    fun getCanceledSalesByPrice_shouldSetCanceledSaleProperty() =
        runTest {
            insertCanceledSales()
            val expected = mapToItem(listOf(canceledSales[0], canceledSales[1]))

            val collectJob = launch { sut.canceledSales.collect() }

            sut.getCanceledSalesByPrice(isOrderedAsc = true)
            advanceUntilIdle()

            assertEquals(expected, sut.canceledSales.value)

            collectJob.cancel()
        }

    private fun mapToItem(canceledSales: List<Sale>) =
        canceledSales.map {
            CommonItem(
                id = it.id,
                photo = it.photo,
                title = it.name,
                subtitle = it.salePrice.toString(),
                description = it.customerName
            )
        }

    private suspend fun insertCanceledSales() = canceledSales.forEach { saleRepository.insert(it, {}, {}) }
}