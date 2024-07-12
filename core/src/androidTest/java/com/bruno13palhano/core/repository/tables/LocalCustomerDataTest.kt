package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.customer.CustomerData
import com.bruno13palhano.core.data.repository.customer.LocalCustomerData
import com.bruno13palhano.core.mocks.makeRandomCustomer
import com.bruno13palhano.core.mocks.makeRandomDataVersion
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class LocalCustomerDataTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var customerTable: CustomerData
    private lateinit var firstCustomer: Customer
    private lateinit var secondCustomer: Customer
    private lateinit var thirdCustomer: Customer
    private lateinit var dataVersion: DataVersion

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        customerTable =
            LocalCustomerData(
                database.customerTableQueries,
                database.versionTableQueries,
                Dispatchers.IO,
            )

        dataVersion = makeRandomDataVersion(id = 1L)
        firstCustomer = makeRandomCustomer(id = 1L, name = "Alan", address = "Rua 15 de Novembro")
        secondCustomer = makeRandomCustomer(id = 2L, name = "Bruno", address = "Rua 13 de Maio")
        thirdCustomer = makeRandomCustomer(id = 3L, name = "Carlos", address = "Rua Walter Rodrigues")
    }

    @Test
    fun shouldInsertCustomerInTheDatabase() =
        runTest {
            customerTable.insert(firstCustomer, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).contains(firstCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCustomersInTheDatabase_ifDatabaseIsNotEmpty() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).containsAnyOf(firstCustomer, secondCustomer, thirdCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() =
        runTest {
            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldUpdateCustomerInTheDatabase_IfCustomerExists() =
        runTest {
            val updateCustomer = makeRandomCustomer(id = 1L)
            customerTable.insert(firstCustomer, dataVersion, {}, {})
            customerTable.update(updateCustomer, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).contains(updateCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldDoNotUpdateCustomerInTheDatabase_IfCustomerNotExists() =
        runTest {
            customerTable.insert(firstCustomer, dataVersion, {}, {})
            customerTable.update(secondCustomer, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).doesNotContain(secondCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldDeleteCustomerWhitThisIdInTheDatabase_ifCustomerExists() =
        runTest {
            insertTwoCustomers()
            customerTable.deleteById(firstCustomer.id, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).doesNotContain(firstCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotDeleteCustomerInTheDatabase_ifCustomerWithThisIdNotExists() =
        runTest {
            insertTwoCustomers()
            customerTable.deleteById(thirdCustomer.id, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                customerTable.getAll().collect {
                    assertThat(it).containsAnyOf(firstCustomer, secondCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCustomersThatMatchesWithThisSearch() =
        runTest {
            val currentCustomer = makeRandomCustomer(id = 1L, "Bruno")
            customerTable.insert(currentCustomer, dataVersion, {}, {})
            customerTable.insert(secondCustomer, dataVersion, {}, {})
            customerTable.insert(thirdCustomer, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                customerTable.search(search = currentCustomer.name).collect {
                    assertThat(it).contains(currentCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesThisSearch() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.search(search = "*").collect {
                    assertThat(it).containsNoneOf(firstCustomer, secondCustomer, thirdCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCustomers_ifThisSearchIsEmpty() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.search(search = "").collect { value ->
                    assertThat(value).containsExactly(firstCustomer, secondCustomer, thirdCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCustomersOrderedByNameDesc() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.getOrderedByName(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(thirdCustomer, secondCustomer, firstCustomer).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCustomersOrderedByNameAsc() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.getOrderedByName(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(firstCustomer, secondCustomer, thirdCustomer).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCustomersOrderedByAddressDesc() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.getOrderedByAddress(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(thirdCustomer, firstCustomer, secondCustomer).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCustomersOrderedByAddressAsc() =
        runTest {
            insertAllCustomers()
            launch(Dispatchers.IO) {
                customerTable.getOrderedByAddress(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(secondCustomer, firstCustomer, thirdCustomer).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnCustomerWithThisId_ifExists() =
        runTest {
            insertTwoCustomers()
            launch(Dispatchers.IO) {
                customerTable.getById(secondCustomer.id).collect {
                    assertThat(it).isEqualTo(secondCustomer)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnLastCustomer_ifExists() =
        runTest {
            insertTwoCustomers()
            launch(Dispatchers.IO) {
                customerTable.getLast().collect {
                    assertThat(it).isEqualTo(secondCustomer)
                    cancel()
                }
            }
        }

    private suspend fun insertTwoCustomers() {
        customerTable.insert(firstCustomer, dataVersion, {}, {})
        customerTable.insert(secondCustomer, dataVersion, {}, {})
    }

    private suspend fun insertAllCustomers() {
        customerTable.insert(firstCustomer, dataVersion, {}, {})
        customerTable.insert(secondCustomer, dataVersion, {}, {})
        customerTable.insert(thirdCustomer, dataVersion, {}, {})
    }
}
