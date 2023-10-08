package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.repository.customer.CustomerLight
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.mocks.makeRandomCustomer
import com.bruno13palhano.core.model.Customer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

@HiltAndroidTest
class CustomerLightTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var customerRepository: CustomerData<Customer>
    private lateinit var zeroIdCustomer: Customer
    private lateinit var firstCustomer: Customer
    private lateinit var secondCustomer: Customer
    private lateinit var thirdCustomer: Customer

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        val customerData = CustomerLight(database.customerTableQueries, Dispatchers.IO)
        customerRepository = CustomerRepository(customerData)

        zeroIdCustomer = makeRandomCustomer(id = 0L)
        firstCustomer = makeRandomCustomer(id = 1L)
        secondCustomer = makeRandomCustomer(id = 2L)
        thirdCustomer = makeRandomCustomer(id = 3L)
    }

    @Test
    fun shouldInsertCustomerInTheDatabase() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).contains(firstCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllCustomersInTheDatabase_ifDatabaseIsNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)
        customerRepository.insert(thirdCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsAnyOf(firstCustomer, secondCustomer, thirdCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldUpdateCustomerInTheDatabase_IfCustomerExists() = runBlocking {
        val latch = CountDownLatch(1)
        val updateCustomer = makeRandomCustomer(id = 1L)
        customerRepository.insert(firstCustomer)
        customerRepository.update(updateCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).contains(updateCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDoNotUpdateCustomerInTheDatabase_IfCustomerNotExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.update(zeroIdCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).doesNotContain(zeroIdCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDeleteCustomerInTheDatabase_ifCustomerExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)

        customerRepository.delete(firstCustomer)

        val job1 = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).doesNotContain(firstCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()
    }

    @Test
    fun shouldDoNothingInTheDatabase_ifCustomerNotExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)

        customerRepository.delete(zeroIdCustomer)

        val job1 = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsAnyOf(firstCustomer, secondCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()
    }

    @Test
    fun shouldDeleteCustomerWhitThisIdInTheDatabase_ifCustomerExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(thirdCustomer)

        customerRepository.deleteById(firstCustomer.id)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).doesNotContain(firstCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDoNothingInTheDatabase_ifCustomerWithThisIdNotExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(thirdCustomer)

        customerRepository.deleteById(zeroIdCustomer.id)

        val job = async(Dispatchers.IO) {
            customerRepository.getAll().take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsAnyOf(firstCustomer, thirdCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCustomersThatMatchesWithThisSearch() = runBlocking {
        val latch = CountDownLatch(1)
        val currentCustomer = makeRandomCustomer(id = 1L, "Bruno")
        customerRepository.insert(currentCustomer)
        customerRepository.insert(secondCustomer)
        customerRepository.insert(thirdCustomer)

        customerRepository.deleteById(zeroIdCustomer.id)

        val job = async(Dispatchers.IO) {
            customerRepository.search(search = currentCustomer.name).take(3).collect { value ->
                latch.countDown()
                assertThat(value).contains(currentCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesThisSearch() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)
        customerRepository.insert(thirdCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.search(search = " ").take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsNoneOf(firstCustomer, secondCustomer, thirdCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllCustomers_ifThisSearchIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)
        customerRepository.insert(thirdCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.search(search = "").take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsExactly(firstCustomer, secondCustomer, thirdCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCustomersOrderedByNameDesc() = runBlocking {
        val latch = CountDownLatch(1)
        val customer1 = makeRandomCustomer(id = 1L, name = "Alan")
        val customer2 = makeRandomCustomer(id = 2L, name = "Bruno")
        val customer3 = makeRandomCustomer(id = 3L, "Carlos")
        customerRepository.insert(customer1)
        customerRepository.insert(customer2)
        customerRepository.insert(customer3)

        val job = async(Dispatchers.IO) {
            customerRepository.getOrderedByName(isOrderedAsc = false).take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsExactly(customer3, customer2, customer1).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCustomersOrderedByNameAsc() = runBlocking {
        val latch = CountDownLatch(1)
        val customer1 = makeRandomCustomer(id = 1L, name = "Alan")
        val customer2 = makeRandomCustomer(id = 2L, name = "Bruno")
        val customer3 = makeRandomCustomer(id = 3L, "Carlos")
        customerRepository.insert(customer1)
        customerRepository.insert(customer2)
        customerRepository.insert(customer3)

        val job = async(Dispatchers.IO) {
            customerRepository.getOrderedByName(isOrderedAsc = true).take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsExactly(customer1, customer2, customer3).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCustomersOrderedByAddressDesc() = runBlocking {
        val latch = CountDownLatch(1)
        val customer1 = makeRandomCustomer(id = 1L, address = "Rua 15 de Novembro")
        val customer2 = makeRandomCustomer(id = 2L, address = "Rua 13 de Maio")
        val customer3 = makeRandomCustomer(id = 3L, address = "Rua Walter Rodrigues")
        customerRepository.insert(customer1)
        customerRepository.insert(customer2)
        customerRepository.insert(customer3)

        val job = async(Dispatchers.IO) {
            customerRepository.getOrderedByAddress(isOrderedAsc = false).take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsExactly(customer3, customer1, customer2).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCustomersOrderedByAddressAsc() = runBlocking {
        val latch = CountDownLatch(1)
        val customer1 = makeRandomCustomer(id = 1L, address = "Rua 15 de Novembro")
        val customer2 = makeRandomCustomer(id = 2L, address = "Rua 13 de Maio")
        val customer3 = makeRandomCustomer(id = 3L, address = "Rua Walter Rodrigues")
        customerRepository.insert(customer1)
        customerRepository.insert(customer2)
        customerRepository.insert(customer3)

        val job = async(Dispatchers.IO) {
            customerRepository.getOrderedByAddress(isOrderedAsc = true).take(3).collect { value ->
                latch.countDown()
                assertThat(value).containsExactly(customer2, customer1, customer3).inOrder()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCustomerWithThisId_ifExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.getById(secondCustomer.id).collect { value ->
                latch.countDown()
                Assert.assertEquals(value, secondCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnLastCustomer_ifExists() = runBlocking {
        val latch = CountDownLatch(1)
        customerRepository.insert(firstCustomer)
        customerRepository.insert(secondCustomer)

        val job = async(Dispatchers.IO) {
            customerRepository.getLast().collect { value ->
                latch.countDown()
                Assert.assertEquals(value, secondCustomer)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}