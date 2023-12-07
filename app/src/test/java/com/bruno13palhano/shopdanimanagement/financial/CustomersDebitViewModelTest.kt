package com.bruno13palhano.shopdanimanagement.financial

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CustomersDebitViewModel
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
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CustomersDebitViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleRepository<Sale>
    private lateinit var sut: CustomersDebitViewModel

    private val debits = listOf(
        makeRandomSale(
            id = 1L,
            stockOrder = makeRandomStockOrder(
                id = 1L,
                product = makeRandomProduct(id =1L, name = "A"),
                salePrice = 25F
            ),
            customer = makeRandomCustomer(id = 1L, name = "A"),
            isPaidByCustomer = false,
            canceled = false
        ),
        makeRandomSale(
            id = 2L,
            stockOrder = makeRandomStockOrder(
                id = 2L,
                product = makeRandomProduct(id =2L, name = "B"),
                salePrice = 50F
            ),
            customer = makeRandomCustomer(id = 1L, name = "B"),
            isPaidByCustomer = false,
            canceled = false
        ),
        makeRandomSale(id = 3L, canceled = true)
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = CustomersDebitViewModel(saleRepository)
    }

    @Test
    fun getDebits_shouldCallGetDebitSalesFromRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = CustomersDebitViewModel(saleRepository)

        whenever(saleRepository.getDebitSales()).doAnswer { flowOf() }

        sut.getDebits()
        advanceUntilIdle()

        verify(saleRepository).getDebitSales()
    }

    @Test
    fun getDebits_shouldSetDebitsProperty() = runTest {
        insertSales()
        val expected = mapToItem(listOf(debits[0], debits[1]))

        val collectJob = launch { sut.debits.collect() }

        sut.getDebits()
        advanceUntilIdle()

        assertEquals(expected, sut.debits.value)

        collectJob.cancel()
    }

    @Test
    fun getDebitByCustomerName_shouldCallGetSalesByCustomerNameFromRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = CustomersDebitViewModel(saleRepository)

        whenever(saleRepository.getSalesByCustomerName(eq(false), any())).doAnswer { flowOf() }

        sut.getDebitByCustomerName(isOrderedAsc = true)
        advanceUntilIdle()

        verify(saleRepository).getSalesByCustomerName(eq(false), any())
    }

    @Test
    fun getDebitByCustomerName_shouldSetDebitsProperty() = runTest {
        insertSales()
        val expected = mapToItem(listOf(debits[1], debits[0]))

        val collectJob = launch { sut.debits.collect() }

        sut.getDebitByCustomerName(isOrderedAsc = false)
        advanceUntilIdle()

        assertEquals(expected, sut.debits.value)

        collectJob.cancel()
    }

    @Test
    fun getDebitBySalePrice_shouldCallGetSalesBySalePriceFromRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = CustomersDebitViewModel(saleRepository)

        whenever(saleRepository.getSalesBySalePrice(eq(false), any())).doAnswer { flowOf() }

        sut.getDebitBySalePrice(isOrderedAsc = false)
        advanceUntilIdle()

        verify(saleRepository).getSalesBySalePrice(eq(false), any())
    }

    @Test
    fun getDebitBySalePrice_shouldSetDebitsProperty() = runTest {
        insertSales()
        val expected = mapToItem(listOf(debits[1], debits[0]))

        val collectJob = launch { sut.debits.collect() }

        sut.getDebitBySalePrice(isOrderedAsc = false)
        advanceUntilIdle()

        assertEquals(expected, sut.debits.value)

        collectJob.cancel()
    }

    private fun mapToItem(debits: List<Sale>) = debits.map {
        CommonItem(
            id = it.id,
            photo = it.photo,
            title = it.customerName,
            subtitle = it.salePrice.toString(),
            description = it.dateOfPayment.toString()
        )
    }

    private suspend fun insertSales() = debits.forEach { saleRepository.insert(it) }
}