package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.data.repository.customer.DefaultCustomerData
import com.bruno13palhano.core.data.repository.delivery.DefaultDeliveryData
import com.bruno13palhano.core.data.repository.product.DefaultProductData
import com.bruno13palhano.core.data.repository.sale.DefaultSaleData
import com.bruno13palhano.core.data.repository.stockorder.DefaultStockOrderData
import com.bruno13palhano.core.mocks.makeRandomCustomer
import com.bruno13palhano.core.mocks.makeRandomDelivery
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.mocks.makeRandomSale
import com.bruno13palhano.core.mocks.makeRandomStockOrder
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.StockOrder
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
class DefaultCategoryDataTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var deliveryTable: DeliveryRepository<Delivery>
    private lateinit var customer1: Customer
    private lateinit var delivery1: Delivery
    private lateinit var delivery2: Delivery
    private lateinit var delivery3: Delivery
    private lateinit var delivery4: Delivery
    private lateinit var delivery5: Delivery
    private lateinit var delivery6: Delivery
    private lateinit var firstItem: StockOrder
    private lateinit var product1: Product

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun setUp() = runTest {
        hiltTestRule.inject()
        init()
    }

    @Test
    fun shouldInsertDeliveryInTheDatabase() = runTest {
        deliveryTable.insert(delivery1)

        launch(Dispatchers.IO) {
            deliveryTable.getAll().collect {
                assertThat(it).containsExactly(delivery1)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateDeliveryInTheDatabase_ifDeliveryExists() = runTest {
        deliveryTable.insert(delivery1)

        val updatedDelivery = makeRandomDelivery(
            id = 1L,
            saleId = 1L,
            customer = customer1,
            price = firstItem.salePrice,
            productName = product1.name
        )

        deliveryTable.update(updatedDelivery)

        launch(Dispatchers.IO) {
            deliveryTable.getAll().collect {
                assertThat(it).contains(updatedDelivery)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateDeliveryInTheDatabase_ifDeliveryNotExists() = runTest {
        deliveryTable.insert(delivery1)
        deliveryTable.update(delivery2)

        launch(Dispatchers.IO) {
            deliveryTable.getAll().collect {
                assertThat(it).doesNotContain(delivery2)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateDeliveryPriceInTheDatabase_ifDeliveryExists() = runTest {
        val updatedPrice = 33.33F
        deliveryTable.insert(delivery1)
        deliveryTable.updateDeliveryPrice(id = delivery1.id, deliveryPrice = updatedPrice)

        launch(Dispatchers.IO) {
            deliveryTable.getById(id = 1L).collect {
                assertThat(it.deliveryPrice).isEqualTo(updatedPrice)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateDeliveryPriceInTheDatabase_ifDeliveryNotExists() = runTest {
        val updatedPrice = 33.33F
        deliveryTable.insert(delivery1)
        deliveryTable.updateDeliveryPrice(id = delivery2.id, deliveryPrice = updatedPrice)

        launch(Dispatchers.IO) {
            deliveryTable.getById(id = 1L).collect {
                assertThat(it.deliveryPrice).isNotEqualTo(updatedPrice)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateDeliveryDateInTheDatabase_ifDeliveryExists() = runTest {
        val updatedDate = 100000000L
        deliveryTable.insert(delivery1)
        deliveryTable.updateDeliveryDate(id = delivery1.id, deliveryDate = updatedDate)

        launch(Dispatchers.IO) {
            deliveryTable.getById(id = 1L).collect {
                assertThat(it.deliveryDate).isEqualTo(updatedDate)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateDeliveryDateInTheDatabase_ifDeliveryNotExists() = runTest {
        val updatedDate = 100000000L
        deliveryTable.insert(delivery1)
        deliveryTable.updateDeliveryDate(id = delivery2.id, deliveryDate = updatedDate)

        launch(Dispatchers.IO) {
            deliveryTable.getById(id = 1L).collect {
                assertThat(it.deliveryDate).isNotEqualTo(updatedDate)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateDeliveredInTheDatabase_ifDeliveryExists() = runTest {
        val updatedDelivered = true
        deliveryTable.insert(delivery1)
        deliveryTable.updateDelivered(id = delivery1.id, delivered = updatedDelivered)

        launch(Dispatchers.IO) {
            deliveryTable.getById(id = 1L).collect {
                assertThat(it.delivered).isEqualTo(updatedDelivered)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotUpdateDeliveredPriceInTheDatabase_ifDeliveryNotExists() = runTest {
        val updatedDelivered = true
        deliveryTable.insert(delivery1)
        deliveryTable.updateDelivered(id = delivery2.id, delivered = updatedDelivered)

        launch(Dispatchers.IO) {
            deliveryTable.getById(id = 1L).collect {
                assertThat(it.delivered).isNotEqualTo(updatedDelivered)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnDeliveriesDelivered_ifExistAny() = runTest {
        insertAllDeliveries()
        launch(Dispatchers.IO) {
            deliveryTable.getDeliveries(delivered = true).collect {
                assertThat(it).containsExactly(delivery5)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifNotExistAnyDeliveriesDelivered() = runTest {
        insertTwoDeliveries()
        launch(Dispatchers.IO) {
            deliveryTable.getDeliveries(delivered = true).collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnDeliveriesNotDelivered_ifExistAny() = runTest {
        insertAllDeliveries()
        launch(Dispatchers.IO) {
            deliveryTable.getDeliveries(delivered = false).collect {
                assertThat(it).containsExactly(delivery1, delivery2, delivery4)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifNotExistAnyDeliveriesNotDelivered() = runTest {
        launch(Dispatchers.IO) {
            deliveryTable.getDeliveries(delivered = false).collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllDeliveries_ifDeliveryNotEmpty() = runTest {
        insertAllDeliveries()
        launch {
            deliveryTable.getAll().collect {
                assertThat(it).containsExactly(delivery1, delivery2, delivery4, delivery5)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDeliveryIsEmpty() = runTest {
        launch {
            deliveryTable.getAll().collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllCanceledDeliveries_ifDeliveryNotEmpty() = runTest {
        insertAllDeliveries()
        launch {
            deliveryTable.getCanceledDeliveries().collect {
                assertThat(it).containsExactly(delivery3, delivery6)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereAreNotCanceledDeliveries() = runTest {
        insertTwoDeliveries()
        launch {
            deliveryTable.getCanceledDeliveries().collect {
                assertThat(it).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteDeliveryWithThisId_ifThisDeliveryExists() = runTest {
        insertTwoDeliveries()
        deliveryTable.deleteById(delivery1.id)

        launch {
            deliveryTable.getAll().collect {
                assertThat(it).doesNotContain(delivery1)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteAnyDelivery_ifThisDeliveryNotExists() = runTest {
        deliveryTable.insert(delivery1)
        deliveryTable.deleteById(delivery2.id)

        launch {
            deliveryTable.getAll().collect {
                assertThat(it).contains(delivery1)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnDeliveryWithThisId_ifThisDeliveryExists() = runTest {
        deliveryTable.insert(delivery1)
        launch {
            deliveryTable.getById(delivery1.id).collect {
                assertThat(it).isEqualTo(delivery1)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastDelivery_ifDeliveryIsNotEmpty() = runTest {
        insertTwoDeliveries()
        launch {
            deliveryTable.getLast().collect {
                assertThat(it).isEqualTo(delivery2)
                cancel()
            }
        }
    }

    private suspend fun insertTwoDeliveries() {
        deliveryTable.insert(delivery1)
        deliveryTable.insert(delivery2)
    }

    private suspend fun insertAllDeliveries() {
        deliveryTable.insert(delivery1)
        deliveryTable.insert(delivery2)
        deliveryTable.insert(delivery3)
        deliveryTable.insert(delivery4)
        deliveryTable.insert(delivery5)
        deliveryTable.insert(delivery6)
    }

    private suspend fun init() {
        val saleTable = DefaultSaleData(
            database.saleTableQueries,
            database.deliveryTableQueries,
            database.stockOrderTableQueries,
            Dispatchers.IO
        )
        val stockOrderTable = DefaultStockOrderData(database.stockOrderTableQueries, Dispatchers.IO)
        val customerTable = DefaultCustomerData(database.customerTableQueries, Dispatchers.IO)
        deliveryTable = DefaultDeliveryData(database.deliveryTableQueries, Dispatchers.IO)
        val productTable = DefaultProductData(
            database.shopDatabaseQueries,
            database.productCategoriesTableQueries,
            Dispatchers.IO
        )

        product1 = makeRandomProduct(id = 1L)
        val product2 = makeRandomProduct(id = 2L)
        val product3 = makeRandomProduct(id = 3L)
        productTable.insert(product1)
        productTable.insert(product2)
        productTable.insert(product3)

        firstItem = makeRandomStockOrder(
            id = 1L,
            product = product1,
            salePrice = 120.90F,
            isOrderedByCustomer = false,
            isPaid = false
        )
        val secondItem = makeRandomStockOrder(
            id = 2L,
            product = product2,
            salePrice = 150.99F,
            isOrderedByCustomer = false,
            isPaid = false
        )
        val thirdItem = makeRandomStockOrder(
            id = 3L,
            product = product3,
            salePrice = 188.80F,
            isOrderedByCustomer = false,
            isPaid = true
        )
        val fourthOrder = makeRandomStockOrder(
            id = 4L,
            product = product3,
            salePrice = 200.99F,
            isOrderedByCustomer = true
        )
        val fifthOrder = makeRandomStockOrder(
            id = 5L,
            product = product2,
            salePrice = 210.99F,
            isOrderedByCustomer = true
        )
        val sixthOrder = makeRandomStockOrder(
            id = 6L,
            product = product1,
            salePrice = 220.77F,
            isOrderedByCustomer = true
        )
        stockOrderTable.insert(firstItem)
        stockOrderTable.insert(secondItem)
        stockOrderTable.insert(thirdItem)
        stockOrderTable.insert(fourthOrder)
        stockOrderTable.insert(fifthOrder)
        stockOrderTable.insert(sixthOrder)

        customer1 = makeRandomCustomer(id = 1L)
        val customer2 = makeRandomCustomer(id = 2L)
        val customer3 = makeRandomCustomer(id = 3L)
        val customer4 = makeRandomCustomer(id = 4L)
        val customer5 = makeRandomCustomer(id = 5L)
        val customer6 = makeRandomCustomer(id = 6L)
        customerTable.insert(customer1)
        customerTable.insert(customer2)
        customerTable.insert(customer3)
        customerTable.insert(customer4)
        customerTable.insert(customer5)
        customerTable.insert(customer6)

        delivery1 = makeRandomDelivery(
            id = 1L,
            saleId = 1L,
            customer = customer1,
            productName = product1.name,
            price = firstItem.salePrice,
            deliveryPrice = 1.2F,
            delivered = false
        )
        delivery2 = makeRandomDelivery(
            id = 2L,
            saleId = 2L,
            customer = customer2,
            productName = product2.name,
            price = secondItem.salePrice,
            deliveryPrice = 2F,
            delivered = false
        )
        delivery3 = makeRandomDelivery(
            id = 3L,
            saleId = 3L,
            customer = customer3,
            productName = product3.name,
            price = thirdItem.salePrice,
            deliveryPrice = 0.0F,
            delivered = true
        )
        delivery4 = makeRandomDelivery(
            id = 4L,
            saleId = 4L,
            customer = customer4,
            productName = product3.name,
            price = fourthOrder.salePrice,
            deliveryPrice = 1.5F,
            delivered = false
        )
        delivery5 = makeRandomDelivery(
            id = 5L,
            saleId = 5L,
            customer = customer5,
            productName = product2.name,
            price = fifthOrder.salePrice,
            deliveryPrice = 3.1F,
            delivered = true
        )
        delivery6 = makeRandomDelivery(
            id = 6L,
            saleId = 6L,
            customer = customer6,
            productName = product1.name,
            price = sixthOrder.salePrice,
            deliveryPrice = 0.0F,
            delivered = false
        )

        val firstStockSale = makeRandomSale(
            id = 1L,
            stockOrder = firstItem,
            customer = customer1,
            delivery = delivery1,
            isPaidByCustomer = false,
            canceled = false
        )
        val secondStockSale = makeRandomSale(
            id = 2L,
            stockOrder = secondItem,
            customer = customer2,
            delivery = delivery2,
            isPaidByCustomer = false,
            canceled = false
        )
        val thirdStockSale = makeRandomSale(
            id = 3L,
            stockOrder = thirdItem,
            customer = customer3,
            delivery = delivery3,
            isPaidByCustomer = false,
            canceled = true
        )
        val fourthOrderSale = makeRandomSale(
            id = 4L,
            stockOrder = fourthOrder,
            customer = customer4,
            delivery = delivery4,
            isPaidByCustomer = true,
            canceled = false
        )
        val fifthOrderSale = makeRandomSale(
            id = 5L,
            stockOrder = fifthOrder,
            customer = customer5,
            delivery = delivery5,
            isPaidByCustomer = true,
            canceled = false
        )
        val sixthOrderSale = makeRandomSale(
            id = 6L,
            stockOrder = sixthOrder,
            customer = customer6,
            delivery = delivery6,
            isPaidByCustomer = false,
            canceled = true
        )

        saleTable.insert(firstStockSale)
        saleTable.insert(secondStockSale)
        saleTable.insert(thirdStockSale)
        saleTable.insert(fourthOrderSale)
        saleTable.insert(fifthOrderSale)
        saleTable.insert(sixthOrderSale)
    }
}