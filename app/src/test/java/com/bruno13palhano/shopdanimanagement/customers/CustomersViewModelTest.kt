package com.bruno13palhano.shopdanimanagement.customers

import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.repository.TestCustomerRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomersViewModel
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
class CustomersViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var customerRepository: CustomerData<Customer>
    private lateinit var sut: CustomersViewModel
    private val customers = listOf(
        makeRandomCustomer(id = 1L, name = "Alex", address = "1"),
        makeRandomCustomer(id = 2L, name = "Bruno", address = "2"),
        makeRandomCustomer(id = 3L, name = "Carlos", address = "3")
    )

    @Before
    fun setup() {
        customerRepository = TestCustomerRepository()
        sut = CustomersViewModel(customerRepository)
    }

    @Test
    fun getAllCustomers_shouldCallGetAllFromRepository() = runTest {
        val customerRepository = mock<CustomerData<Customer>>()
        val sut = CustomersViewModel(customerRepository)

        whenever(customerRepository.getAll()).doAnswer { flowOf() }

        sut.getAllCustomers()
        advanceUntilIdle()

        verify(customerRepository).getAll()
    }

    @Test
    fun getAllCustomers_shouldSetCustomerListProperty() = runTest {
        insertCustomers()

        val collectJob = launch { sut.customerList.collect() }
        sut.getAllCustomers()
        advanceUntilIdle()

        assertEquals(mapToItem(customers), sut.customerList.value)
        collectJob.cancel()
    }

    @Test
    fun getOrderedByName_shouldCallGetOrderedByNameFromRepository() = runTest {
        val customerRepository = mock<CustomerData<Customer>>()
        val sut = CustomersViewModel(customerRepository)

        whenever(customerRepository.getOrderedByName(any())).doAnswer { flowOf() }

        sut.getOrderedByName(isOrderedAsc = true)
        advanceUntilIdle()

        verify(customerRepository).getOrderedByName(any())
    }

    @Test
    fun getOrderedByName_shouldSetCustomerListProperty() = runTest {
        insertCustomers()

        val collectJob = launch { sut.customerList.collect() }
        sut.getOrderedByName(isOrderedAsc = true)
        advanceUntilIdle()

        assertEquals(mapToItem(customers), sut.customerList.value)
        collectJob.cancel()
    }

    @Test
    fun getOrderedByAddress_shouldCallGetOrderedByAddressFromRepository() = runTest {
        val customerRepository = mock<CustomerData<Customer>>()
        val sut = CustomersViewModel(customerRepository)

        whenever(customerRepository.getOrderedByAddress(any())).doAnswer { flowOf() }

        sut.getOrderedByAddress(isOrderedAsc = true)
        advanceUntilIdle()

        verify(customerRepository).getOrderedByAddress(any())
    }

    @Test
    fun getOrderedByAddress_shouldSetCustomerListProperty() = runTest {
        insertCustomers()

        val collectJob = launch { sut.customerList.collect() }
        sut.getOrderedByAddress(isOrderedAsc = true)
        advanceUntilIdle()

        assertEquals(mapToItem(customers), sut.customerList.value)
        collectJob.cancel()
    }

    private fun mapToItem(customers: List<Customer>) = customers.map {
        CommonItem(
            id = it.id,
            photo = it.photo,
            title = it.name,
            subtitle = it.address,
            description = it.email
        )
    }

    private suspend fun insertCustomers() = customers.forEach { customerRepository.insert(it) }
}