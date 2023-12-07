package com.bruno13palhano.shopdanimanagement.customers

import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.repository.TestCustomerRepository
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerInfoViewModel
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
class CustomerInfoViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var customerRepository: CustomerRepository<Customer>
    private lateinit var saleRepository: SaleRepository<Sale>
    private lateinit var sut: CustomerInfoViewModel

    private val customers = listOf(
        makeRandomCustomer(id = 1L),
        makeRandomCustomer(id = 2L),
        makeRandomCustomer(id = 3L)
    )
    private val sales = listOf(
        makeRandomSale(id = 1L, customer = customers[0]),
        makeRandomSale(id = 2L, customer = customers[0]),
        makeRandomSale(id = 3L, customer = customers[1]),
        makeRandomSale(id = 4L, customer = customers[1]),
        makeRandomSale(id = 5L, customer = customers[2]),
        makeRandomSale(id = 6L, customer = customers[2]),
    )

    @Before
    fun setup() {
        customerRepository = TestCustomerRepository()
        saleRepository = TestSaleRepository()
        sut = CustomerInfoViewModel(customerRepository, saleRepository)
    }

    @Test
    fun getCustomerInfo_shouldCallGetByCustomerIdFromSaleRepository() = runTest {
        val customerRepository = mock<CustomerRepository<Customer>>()
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = CustomerInfoViewModel(customerRepository, saleRepository)

        whenever(customerRepository.getById(any())).doAnswer { flowOf() }
        whenever(saleRepository.getByCustomerId(any())).doAnswer { flowOf() }

        sut.getCustomerInfo(customerId = 1L)
        advanceUntilIdle()

        verify(saleRepository).getByCustomerId(any())
    }

    @Test
    fun getCustomerInfo_shouldCallGetByIdFromCustomerRepository() = runTest {
        val customerRepository = mock<CustomerRepository<Customer>>()
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = CustomerInfoViewModel(customerRepository, saleRepository)

        whenever(customerRepository.getById(any())).doAnswer { flowOf() }
        whenever(saleRepository.getByCustomerId(any())).doAnswer { flowOf() }

        sut.getCustomerInfo(customerId = 1L)
        advanceUntilIdle()

        verify(customerRepository).getById(any())
    }

    @Test
    fun getCustomerInfo_shouldSetCustomerInfoProperty() = runTest {
        insertCustomers()
        insertSales()

        val customer = customers[0]
        val customerSales = listOf(sales[0], sales[1])

        val collectJob = launch { sut.customerInfo.collect() }
        sut.getCustomerInfo(customerId = customer.id)
        sut.getCustomerPurchases(customerId = customer.id)

        advanceUntilIdle()

        assertEquals(mapToCustomerInfo(customerSales, customer), sut.customerInfo.value)

        collectJob.cancel()
    }

    @Test
    fun getCustomerPurchases_shouldCallGetByCustomerIdFromSaleRepository() = runTest{
        val customerRepository = mock<CustomerRepository<Customer>>()
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = CustomerInfoViewModel(customerRepository, saleRepository)

        whenever(saleRepository.getByCustomerId(any())).doAnswer { flowOf() }

        sut.getCustomerPurchases(customerId = 1L)
        advanceUntilIdle()

        verify(saleRepository).getByCustomerId(any())
    }

    private fun mapToCustomerInfo(sales: List<Sale>, customer: Customer): CustomerInfoViewModel.CustomerInfo {
        var owingValue = 0F
        var purchasesValue = 0F
        var lastPurchaseValue = 0F
        sales.filter { !it.isPaidByCustomer }.map { owingValue += it.salePrice }
        sales.map { purchasesValue += it.salePrice }
        sales.map { sale -> if (sale.salePrice != 0F) lastPurchaseValue = sale.salePrice }

        return CustomerInfoViewModel.CustomerInfo(
            name = customer.name,
            address = customer.address,
            photo = customer.photo,
            owingValue = owingValue.toString(),
            purchasesValue = purchasesValue.toString(),
            lastPurchaseValue = lastPurchaseValue.toString()
        )
    }

    private suspend fun insertCustomers() = customers.forEach { customerRepository.insert(it) }
    private suspend fun insertSales() = sales.forEach { saleRepository.insert(it) }
}