package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.customer.DefaultCustomerData
import com.bruno13palhano.core.data.repository.product.LocalProductData
import com.bruno13palhano.core.data.repository.sale.DefaultSaleData
import com.bruno13palhano.core.data.repository.sale.SaleData
import com.bruno13palhano.core.data.repository.stock.DefaultStockData
import com.bruno13palhano.core.mocks.makeRandomCustomer
import com.bruno13palhano.core.mocks.makeRandomDataVersion
import com.bruno13palhano.core.mocks.makeRandomDelivery
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.mocks.makeRandomSale
import com.bruno13palhano.core.mocks.makeRandomStockOrder
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockItem
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
class DefaultSaleDataTest {
    @Inject
    lateinit var database: ShopDatabase
    private lateinit var saleTable: SaleData
    private lateinit var firstStockSale: Sale
    private lateinit var secondStockSale: Sale
    private lateinit var thirdStockSale: Sale
    private lateinit var fourthOrderSale: Sale
    private lateinit var fifthOrderSale: Sale
    private lateinit var sixthOrderSale: Sale
    private lateinit var updatedSale: Sale
    private lateinit var delivery4: Delivery
    private lateinit var customer4: Customer
    private lateinit var fourthItem: StockItem
    private lateinit var dataVersion: DataVersion

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun setUp() =
        runTest {
            hiltTestRule.inject()
            init()
        }

    @Test
    fun shouldInsertSaleInTheDatabase() =
        runTest {
            insertSales(amount = 1)
            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).contains(firstStockSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldUpdateSaleInTheDatabase_ifExists() =
        runTest {
            insertSales(amount = 1)
            saleTable.update(updatedSale, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).contains(updatedSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotUpdateSaleInTheDatabase_ifNotExists() =
        runTest {
            insertSales(amount = 1)
            saleTable.update(secondStockSale, dataVersion, {}, {})

            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).doesNotContain(secondStockSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldSetCanceledPropertyToTrue_ifSaleExists() =
        runTest {
            insertSales(amount = 2)
            saleTable.cancelSale(secondStockSale.id)

            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).doesNotContain(secondStockSale)
                    cancel()
                }
            }
        }

    @Test(expected = NullPointerException::class)
    fun shouldThrowNullPointerException_whenUpdateCanceledProperty_ifSaleNotExists() =
        runTest {
            insertSales(amount = 2)
            saleTable.cancelSale(saleId = 7L)
        }

    @Test
    fun shouldInsertOrderSaleDeliveryItems_inTheDatabase() =
        runTest {
            insertSales(amount = 3)
            saleTable.insert(
                model = fourthOrderSale,
                version = dataVersion,
                pushed = true,
                onSuccess = { _, _ -> },
                onError = {},
            )

            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).contains(fourthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnSalesForThisCustomer_ifExists() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getByCustomerId(sixthOrderSale.customerId).collect {
                    assertThat(it).contains(sixthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoSalesForThisCustomer() =
        runTest {
            insertSales(amount = 1)
            launch(Dispatchers.IO) {
                saleTable.getByCustomerId(secondStockSale.customerId).collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnLastSalesByThisLimit_ifDatabaseIsNotEmpty() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getLastSales(0, 3).collect {
                    assertThat(it).containsExactly(fourthOrderSale, fifthOrderSale, sixthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockSaleInThisRange_ifExists() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getAllStockSales(0, 3).collect {
                    assertThat(it).containsExactly(firstStockSale, secondStockSale, thirdStockSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoStockSalesExists() =
        runTest {
            launch(Dispatchers.IO) {
                saleTable.getAllStockSales(0, 3).collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllOrdersSaleInThisRange_ifExists() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getAllOrdersSales(0, 2).collect {
                    assertThat(it).containsExactly(fifthOrderSale, sixthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoOrdersSalesExists() =
        runTest {
            launch(Dispatchers.IO) {
                saleTable.getAllOrdersSales(0, 2).collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllSales_ifDatabaseIsNotEmpty() =
        runTest {
            insertSales(amount = 3)
            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).containsExactly(firstStockSale, secondStockSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() =
        runTest {
            launch(Dispatchers.IO) {
                saleTable.getAll().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSales_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getAllCanceledSales().collect {
                    assertThat(it).containsExactly(thirdStockSale, sixthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoCanceledSales() =
        runTest {
            insertSales(amount = 2)
            launch(Dispatchers.IO) {
                saleTable.getAllCanceledSales().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSalesOrderedByNameDesc_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getCanceledByName(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(thirdStockSale, sixthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSalesOrderedByNameAsc_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getCanceledByName(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(sixthOrderSale, thirdStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSalesOrderedByCustomerNameDesc_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getCanceledByCustomerName(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(sixthOrderSale, thirdStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSalesOrderedByCustomerNameAsc_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getCanceledByCustomerName(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(thirdStockSale, sixthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSalesOrderedByPriceDesc_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getCanceledByPrice(isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(sixthOrderSale, thirdStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllCanceledSalesOrderedByPriceAsc_ifThereAreCanceledSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getCanceledByPrice(isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(thirdStockSale, sixthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllOrdersSalesPaidOrderedByCustomerNameDesc_ifThereAreOrdersSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesByCustomerName(isPaidByCustomer = true, isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(fifthOrderSale, fourthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllOrdersSalesPaidOrderedByCustomerNameAsc_ifThereAreOrdersSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesByCustomerName(isPaidByCustomer = true, isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(fourthOrderSale, fifthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockSalesUnPaidOrderedByCustomerNameDesc_ifThereAreStockSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesByCustomerName(isPaidByCustomer = false, isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(secondStockSale, firstStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockSalesUnPaidOrderedByCustomerNameAsc_ifThereAreStockSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesByCustomerName(isPaidByCustomer = false, isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(firstStockSale, secondStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllOrdersSalesPaidOrderedBySalePriceDesc_ifThereAreOrdersSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesBySalePrice(isPaidByCustomer = true, isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(fifthOrderSale, fourthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllOrdersSalesPaidOrderedBySalePriceAsc_ifThereAreOrdersSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesBySalePrice(isPaidByCustomer = true, isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(fourthOrderSale, fifthOrderSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockSalesUnPaidOrderedBySalePriceDesc_ifThereAreStockSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesBySalePrice(isPaidByCustomer = false, isOrderedAsc = false).collect {
                    assertThat(it).containsExactly(secondStockSale, firstStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllStockSalesUnPaidOrderedBySalePriceAsc_ifThereAreStockSales() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getSalesBySalePrice(isPaidByCustomer = false, isOrderedAsc = true).collect {
                    assertThat(it).containsExactly(firstStockSale, secondStockSale).inOrder()
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnSaleWithThisId_ifSaleExists() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getById(fourthOrderSale.id).collect {
                    assertThat(it).isEqualTo(fourthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnLastSale_ifSaleExists() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getLast().collect {
                    assertThat(it).isEqualTo(sixthOrderSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllSalesInDebit_ifThereAreSalesInDebit() =
        runTest {
            insertSales(amount = 6)
            launch(Dispatchers.IO) {
                saleTable.getDebitSales().collect {
                    println(it)
                    assertThat(it).containsExactly(firstStockSale, secondStockSale)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifThereAreNoSalesInDebit() =
        runTest {
            launch(Dispatchers.IO) {
                saleTable.getDebitSales().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    private suspend fun insertSales(amount: Int) {
        when (amount) {
            1 -> {
                saleTable.insert(firstStockSale, dataVersion, true, {}, { _, _ -> })
            }
            2 -> {
                saleTable.insert(firstStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(secondStockSale, dataVersion, true, {}, { _, _ -> })
            }
            3 -> {
                saleTable.insert(firstStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(secondStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(thirdStockSale, dataVersion, true, {}, { _, _ -> })
            }
            4 -> {
                saleTable.insert(firstStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(secondStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(thirdStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(fourthOrderSale, dataVersion, true, {}, { _, _ -> })
            }
            5 -> {
                saleTable.insert(firstStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(secondStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(thirdStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(fourthOrderSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(fifthOrderSale, dataVersion, true, {}, { _, _ -> })
            }
            else -> {
                saleTable.insert(firstStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(secondStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(thirdStockSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(fourthOrderSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(fifthOrderSale, dataVersion, true, {}, { _, _ -> })
                saleTable.insert(sixthOrderSale, dataVersion, true, {}, { _, _ -> })
            }
        }
    }

    private suspend fun init() {
        saleTable =
            DefaultSaleData(
                database.saleTableQueries,
                database.stockTableQueries,
                database.versionTableQueries,
                Dispatchers.IO,
            )

        dataVersion = makeRandomDataVersion(id = 1L)
        val stockTable =
            DefaultStockData(
                database.stockTableQueries,
                database.versionTableQueries,
                Dispatchers.IO,
            )
        val customerTable =
            DefaultCustomerData(
                database.customerTableQueries,
                database.versionTableQueries,
                Dispatchers.IO,
            )
        val productTable =
            LocalProductData(
                database.shopDatabaseQueries,
                database.productCategoriesTableQueries,
                database.versionTableQueries,
                Dispatchers.IO,
            )
        val product1 =
            makeRandomProduct(
                id = 1L,
                name = "Homem",
                categories = listOf(Category(id = 1L, category = "Perfumes", timestamp = "")),
            )
        val product2 =
            makeRandomProduct(
                id = 2L,
                name = "Kaiak",
                categories =
                    listOf(
                        Category(id = 2L, category = "Soaps", timestamp = ""),
                        Category(id = 1L, category = "Perfumes", timestamp = ""),
                    ),
            )
        val product3 =
            makeRandomProduct(
                id = 3L,
                name = "Luna",
                categories = listOf(Category(id = 3L, category = "Others", timestamp = "")),
            )
        productTable.insert(product1, dataVersion, {}, {})
        productTable.insert(product2, dataVersion, {}, {})
        productTable.insert(product3, dataVersion, {}, {})

        val firstItem =
            makeRandomStockOrder(
                id = 1L,
                product = product1,
                salePrice = 120.90F,
                isPaid = false,
            )
        val secondItem =
            makeRandomStockOrder(
                id = 2L,
                product = product2,
                salePrice = 150.99F,
                isPaid = false,
            )
        val thirdItem =
            makeRandomStockOrder(
                id = 3L,
                product = product3,
                salePrice = 188.80F,
                isPaid = true,
            )
        fourthItem =
            makeRandomStockOrder(
                id = 4L,
                product = product3,
                salePrice = 200.99F,
            )
        val fifthItem =
            makeRandomStockOrder(
                id = 5L,
                product = product2,
                salePrice = 210.99F,
            )
        val sixthItem =
            makeRandomStockOrder(
                id = 6L,
                product = product1,
                salePrice = 220.77F,
            )

        stockTable.insert(firstItem, dataVersion, {}, {})
        stockTable.insert(secondItem, dataVersion, {}, {})
        stockTable.insert(thirdItem, dataVersion, {}, {})
        stockTable.insert(fourthItem, dataVersion, {}, {})
        stockTable.insert(fifthItem, dataVersion, {}, {})
        stockTable.insert(sixthItem, dataVersion, {}, {})

        val customer1 = makeRandomCustomer(id = 1L, name = "Brenda")
        val customer2 = makeRandomCustomer(id = 2L, name = "Bruno")
        val customer3 = makeRandomCustomer(id = 3L, name = "Daniela")
        customer4 = makeRandomCustomer(id = 4L, name = "Fernando")
        val customer5 = makeRandomCustomer(id = 5L, name = "Helena")
        val customer6 = makeRandomCustomer(id = 6L, name = "Josué")

        customerTable.insert(customer1, dataVersion, {}, {})
        customerTable.insert(customer2, dataVersion, {}, {})
        customerTable.insert(customer3, dataVersion, {}, {})
        customerTable.insert(customer4, dataVersion, {}, {})
        customerTable.insert(customer5, dataVersion, {}, {})
        customerTable.insert(customer6, dataVersion, {}, {})

        val delivery1 =
            makeRandomDelivery(
                id = 1L,
                saleId = 1L,
                customer = customer1,
                productName = product1.name,
            )
        val delivery2 =
            makeRandomDelivery(
                id = 2L,
                saleId = 2L,
                customer = customer2,
                productName = product2.name,
            )
        val delivery3 =
            makeRandomDelivery(
                id = 3L,
                saleId = 3L,
                customer = customer3,
                productName = product3.name,
                deliveryPrice = 0.0F,
            )
        delivery4 =
            makeRandomDelivery(
                id = 4L,
                saleId = 4L,
                customer = customer4,
                productName = product3.name,
            )
        val delivery5 =
            makeRandomDelivery(
                id = 5L,
                saleId = 5L,
                customer = customer5,
                productName = product2.name,
            )
        val delivery6 =
            makeRandomDelivery(
                id = 6L,
                saleId = 6L,
                customer = customer6,
                productName = product1.name,
                deliveryPrice = 0.0F,
            )

        firstStockSale =
            makeRandomSale(
                id = 1L,
                stockItem = firstItem,
                customer = customer1,
                delivery = delivery1,
                isOrderedByCustomer = false,
                isPaidByCustomer = false,
                canceled = false,
            )
        secondStockSale =
            makeRandomSale(
                id = 2L,
                stockItem = secondItem,
                customer = customer2,
                delivery = delivery2,
                isOrderedByCustomer = false,
                isPaidByCustomer = false,
                canceled = false,
            )
        thirdStockSale =
            makeRandomSale(
                id = 3L,
                stockItem = thirdItem,
                customer = customer3,
                delivery = delivery3,
                isOrderedByCustomer = false,
                isPaidByCustomer = false,
                canceled = true,
            )
        fourthOrderSale =
            makeRandomSale(
                id = 4L,
                stockItem = fourthItem,
                customer = customer4,
                delivery = delivery4,
                isOrderedByCustomer = true,
                isPaidByCustomer = true,
                canceled = false,
            )
        fifthOrderSale =
            makeRandomSale(
                id = 5L,
                stockItem = fifthItem,
                customer = customer5,
                delivery = delivery5,
                isOrderedByCustomer = true,
                isPaidByCustomer = true,
                canceled = false,
            )
        sixthOrderSale =
            makeRandomSale(
                id = 6L,
                stockItem = sixthItem,
                customer = customer6,
                delivery = delivery6,
                isOrderedByCustomer = true,
                isPaidByCustomer = false,
                canceled = true,
            )
        updatedSale =
            makeRandomSale(
                id = 1L,
                quantity = 1000000,
                stockItem = firstItem,
                customer = customer1,
                delivery = delivery1,
                canceled = false,
            )
    }
}
