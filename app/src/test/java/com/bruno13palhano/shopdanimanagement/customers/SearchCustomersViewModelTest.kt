package com.bruno13palhano.shopdanimanagement.customers

import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.makeRandomSearchCache
import com.bruno13palhano.shopdanimanagement.repository.TestCustomerRepository
import com.bruno13palhano.shopdanimanagement.repository.TestSearchCacheRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.SearchCustomersViewModel
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
class SearchCustomersViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var customerRepository: CustomerRepository<Customer>
    private lateinit var searchCacheRepository: SearchCacheRepository<SearchCache>
    private lateinit var sut: SearchCustomersViewModel

    @Before
    fun setup() {
        customerRepository = TestCustomerRepository()
        searchCacheRepository = TestSearchCacheRepository()
        sut = SearchCustomersViewModel(customerRepository, searchCacheRepository)
    }

    @Test
    fun searchCache_shouldGetAllSearch() = runTest {
        val cache = listOf(makeRandomSearchCache(), makeRandomSearchCache(), makeRandomSearchCache())
        cache.forEach { searchCacheRepository.insert(it) }

        val collectJob = launch { sut.searchCache.collect() }
        advanceUntilIdle()

        assertEquals(cache, sut.searchCache.value)

        collectJob.cancel()
    }

    @Test
    fun search_shouldCallSearchFromCustomerRepository() = runTest {
        val searchCacheRepository = mock<SearchCacheRepository<SearchCache>>()
        val customerRepository = mock<CustomerRepository<Customer>>()
        val sut = SearchCustomersViewModel(customerRepository, searchCacheRepository)

        whenever(customerRepository.search(any())).doAnswer { flowOf() }
        val search = "test"

        sut.search(search = search)
        advanceUntilIdle()

        verify(customerRepository).search(any())
    }

    @Test
    fun search_shouldSetCustomersProperty() = runTest {
        val customers = listOf(
            makeRandomCustomer(id = 1L),
            makeRandomCustomer(id = 2L),
            makeRandomCustomer(id = 3L)
        )
        customers.forEach { customerRepository.insert(it) }

        val collectJob = launch { sut.customers.collect() }

        val current = customers[0]
        val search = current.name

        sut.search(search = search)
        advanceUntilIdle()

        assertEquals(mapToItem(listOf(current)), sut.customers.value)

        collectJob.cancel()
    }

    @Test
    fun insert_shouldCallInsertFromSearchCacheRepository() = runTest {
        val customerRepository = mock<CustomerRepository<Customer>>()
        val searchCacheRepository = mock<SearchCacheRepository<SearchCache>>()
        val sut = SearchCustomersViewModel(customerRepository, searchCacheRepository)

        val search = "test"
        sut.insertCache(search = search)
        advanceUntilIdle()

        verify(searchCacheRepository).insert(any())
    }

    private fun mapToItem(customers: List<Customer>) = customers.map {
        CommonItem(
            id = it.id,
            photo = it.photo,
            title = it.name,
            subtitle = it.email,
            description = it.address
        )
    }
}