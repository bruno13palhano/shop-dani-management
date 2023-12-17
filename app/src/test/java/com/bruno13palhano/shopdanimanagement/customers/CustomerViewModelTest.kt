package com.bruno13palhano.shopdanimanagement.customers

import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.repository.TestCustomerRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerViewModel
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
class CustomerViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var customerRepository: CustomerRepository
    private lateinit var sut: CustomerViewModel

    @Before
    fun setup() {
        customerRepository = TestCustomerRepository()
        sut = CustomerViewModel(customerRepository)
    }

    @Test
    fun updateName_shouldChangeName() {
        val name = "name"
        sut.updateName(name = name)

        assertEquals(name, sut.name)
    }

    @Test
    fun updatePhoto_shouldChangePhoto() {
        val photo = byteArrayOf()
        sut.updatePhoto(photo = photo)

        assertEquals(photo, sut.photo)
    }

    @Test
    fun updateEmail_shouldChangeEmail() {
        val email = "email"
        sut.updateEmail(email = email)

        assertEquals(email, sut.email)
    }

    @Test
    fun updateAddress_shouldChangeAddress() {
        val address = "address"
        sut.updateAddress(address = address)

        assertEquals(address, sut.address)
    }

    @Test
    fun updatePhoneNumber_shouldChangePhoneNumber() {
        val phoneNumber = "phoneNumber"
        sut.updatePhoneNumber(phoneNumber = phoneNumber)

        assertEquals(phoneNumber, sut.phoneNumber)
    }

    @Test
    fun isCustomerNotEmpty_shouldReturnTrue_ifNameAndAddressIsNotEmpty() = runTest {
        sut.updateName(name = "name")
        sut.updateAddress(address = "address")

        val collectJob = launch { sut.isCustomerNotEmpty.collect() }
        advanceUntilIdle()

        assertEquals(true, sut.isCustomerNotEmpty.value)

        collectJob.cancel()
    }

    @Test
    fun isCustomerNotEmpty_shouldReturnFalse_ifNameOrAddressNotEmpty() = runTest {
        sut.updateName(name = "")
        sut.updateAddress(address = "address")

        val collectJob = launch { sut.isCustomerNotEmpty.collect() }
        advanceUntilIdle()

        assertEquals(false, sut.isCustomerNotEmpty.value)

        collectJob.cancel()
    }

    @Test
    fun getCustomer_shouldCallGetByIdFromRepository() = runTest {
        val customerRepository = mock<CustomerRepository>()
        val sut = CustomerViewModel(customerRepository)

        whenever(customerRepository.getById(any())).doAnswer { flowOf() }

        sut.getCustomer(id = 1L)
        advanceUntilIdle()

        verify(customerRepository).getById(any())
    }

    @Test
    fun getCustomer_shouldSetCustomerProperties() = runTest {
        val customer = makeRandomCustomer(id = 1L)
        customerRepository.insert(model = customer, {}, {})

        sut.getCustomer(id = 1L)
        advanceUntilIdle()

        assertEquals(customer.name, sut.name)
        assertEquals(customer.photo, sut.photo)
        assertEquals(customer.email, sut.email)
        assertEquals(customer.address, sut.address)
        assertEquals(customer.phoneNumber, sut.phoneNumber)
    }

    @Test
    fun insertCustomer_shouldCallInsertFromRepository() = runTest {
        val customerRepository = mock<CustomerRepository>()
        val sut = CustomerViewModel(customerRepository)

        sut.insertCustomer({}, {})
        advanceUntilIdle()

        verify(customerRepository).insert(any(), any(), any())
    }

    @Test
    fun updateCustomer_shouldCallUpdateFromRepository() = runTest {
        val customerRepository = mock<CustomerRepository>()
        val sut = CustomerViewModel(customerRepository)

        sut.updateCustomer(id = 1L, {}, {})
        advanceUntilIdle()

        verify(customerRepository).update(any(), any(), any())
    }
}