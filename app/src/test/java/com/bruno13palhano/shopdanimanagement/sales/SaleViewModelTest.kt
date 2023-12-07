package com.bruno13palhano.shopdanimanagement.sales

import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCustomer
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestCustomerRepository
import com.bruno13palhano.shopdanimanagement.repository.TestProductRepository
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.repository.TestStockOrderRepository
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SaleViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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
class SaleViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleRepository<Sale>
    private lateinit var stockOrderRepository: StockOrderRepository<StockOrder>
    private lateinit var productRepository: ProductRepository<Product>
    private lateinit var customerRepository: CustomerRepository<Customer>
    private lateinit var sut: SaleViewModel

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        stockOrderRepository = TestStockOrderRepository()
        productRepository = TestProductRepository()
        customerRepository = TestCustomerRepository()
        sut = SaleViewModel(saleRepository, stockOrderRepository, productRepository, customerRepository)
    }

    @Test
    fun isSaleNotEmpty_shouldReturnTrue_whenPropertiesIsNotEmpty() = runTest {
        sut.updateQuantity(quantity = "10")
        sut.updatePurchasePrice(purchasePrice = "123.12")
        sut.updateSalePrice(salePrice = "321.45")

        val collectJob = launch { sut.isSaleNotEmpty.collect() }
        advanceUntilIdle()

        assertEquals(true, sut.isSaleNotEmpty.value)

        collectJob.cancel()
    }

    @Test
    fun isSaleNotEmpty_shouldReturnFalse_whenPropertiesIsEmpty() = runTest {
        sut.updateQuantity(quantity = "")
        sut.updatePurchasePrice(purchasePrice = "123.12")
        sut.updateSalePrice(salePrice = "321.45")

        val collectJob = launch { sut.isSaleNotEmpty.collect() }
        advanceUntilIdle()

        assertEquals(false, sut.isSaleNotEmpty.value)

        collectJob.cancel()
    }

    @Test
    fun updateQuantity_shouldChangeQuantity() {
        val quantity = "10"
        sut.updateQuantity(quantity = quantity)

        assertEquals(quantity, sut.quantity)
    }

    @Test
    fun updateDateOfSale_shouldChangeDateOfSale() {
        val dateOfSale = 1000L
        sut.updateDateOfSale(dateOfSale = dateOfSale)

        assertEquals(dateOfSale, sut.dateOfSale)
    }

    @Test
    fun updateDateOfPayment_shouldChangeDateOfPayment() {
        val dateOfPayment = 2000L
        sut.updateDateOfPayment(dateOfPayment = dateOfPayment)

        assertEquals(dateOfPayment, sut.dateOfPayment)
    }

    @Test
    fun updatePurchasePrice_shouldChangePurchasePrice() {
        val purchasePrice = "123.45"
        sut.updatePurchasePrice(purchasePrice = purchasePrice)

        assertEquals(purchasePrice, sut.purchasePrice)
    }

    @Test
    fun updateSalePrice_shouldChangeSalePrice() {
        val salePrice = "543.21"
        sut.updateSalePrice(salePrice = salePrice)

        assertEquals(salePrice, sut.salePrice)
    }

    @Test
    fun updateDeliveryPrice_shouldChangeDeliveryPrice() {
        val deliveryPrice = "123.45"
        sut.updateDeliveryPrice(deliveryPrice = deliveryPrice)

        assertEquals(deliveryPrice, sut.deliveryPrice)
    }

    @Test
    fun updateIdPaidByCustomer_shouldChangeIsPaidByCustomer() {
        val isPaidByCustomer = true
        sut.updateIsPaidByCustomer(isPaidByCustomer = isPaidByCustomer)

        assertEquals(isPaidByCustomer, sut.isPaidByCustomer)
    }

    @Test
    fun updateCustomerName_shouldChangeCustomerName() {
        val customerName = "test"
        sut.updateCustomerName(customerName = customerName)

        assertEquals(customerName, sut.customerName)
    }

    @Test
    fun updateCustomerName_shouldCheckCustomer() = runTest {
        val customers = listOf(makeRandomCustomer(id = 1L), makeRandomCustomer(id = 2L))
        customers.forEach { customerRepository.insert(it) }

        sut.getAllCustomers()
        advanceUntilIdle()

        val customerName = customers[0].name
        sut.updateCustomerName(customerName = customerName)

        val customerChecked = sut.allCustomers.filter { it.isChecked }

        assertEquals(customerName, customerChecked[0].name)
    }

    @Test
    fun getAllCustomers_shouldCallGetAllFromCustomerRepository() = runTest {
        val customerRepository = mock<CustomerRepository<Customer>>()
        val sut = makeSutFromMocks(customerRep = customerRepository)

        whenever(customerRepository.getAll()).doAnswer { flow {  } }

        sut.getAllCustomers()
        advanceUntilIdle()

        verify(customerRepository).getAll()
    }

    @Test
    fun getAllCustomers_shouldSetAllCustomersProperty() = runTest {
        val customers = listOf(makeRandomCustomer(id = 1L), makeRandomCustomer(id = 2L))
        customers.forEach { customerRepository.insert(it) }

        sut.getAllCustomers()
        advanceUntilIdle()

        assertEquals(mapToCustomerCheck(customers), sut.allCustomers)
    }

    @Test
    fun getProduct_shouldCallGetByIdFromProductRepository() = runTest {
        val productRepository = mock<ProductRepository<Product>>()
        val sut = makeSutFromMocks(productRep = productRepository)

        whenever(productRepository.getById(any())).doAnswer { flowOf() }

        sut.getProduct(id = 1L)
        advanceUntilIdle()

        verify(productRepository).getById(any())
    }

    @Test
    fun getProduct_shouldSetSalePropertiesThatDependOnProduct() = runTest {
        val product = makeRandomProduct(id = 1L)
        productRepository.insert(model = product)

        sut.getProduct(id = 1L)
        advanceUntilIdle()

        assertEquals(product.name, sut.productName)
        assertEquals(product.photo, sut.photo)
        assertEquals(product.categories.joinToString(", ") { it.category }, sut.category)
        assertEquals(product.company, sut.company)
    }

    @Test
    fun getStockItem_shouldCallGetByIdFromStockOrderRepository() = runTest {
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val sut = makeSutFromMocks(stockOrderRep = stockOrderRepository)

        whenever(stockOrderRepository.getById(any())).doAnswer { flowOf() }

        sut.getStockItem(stockId = 1L)
        advanceUntilIdle()

        verify(stockOrderRepository).getById(any())
    }

    @Test
    fun getStockItem_shouldSetSalePropertiesThatDependOnStockOrder() = runTest {
        val item = makeRandomStockOrder(id = 1L)
        stockOrderRepository.insert(model = item)

        sut.getStockItem(stockId = 1L)
        advanceUntilIdle()

        assertEquals(item.name, sut.productName)
        assertEquals(item.photo, sut.photo)
        assertEquals(item.purchasePrice.toString(), sut.purchasePrice)
        assertEquals(item.salePrice.toString(), sut.salePrice)
        assertEquals(item.categories.joinToString(", ") { it.category }, sut.category)
        assertEquals(item.company, sut.company)
        assertEquals(item.quantity, sut.stockQuantity)
    }

    @Test
    fun getSale_shouldCallGetByIdFromSaleRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = makeSutFromMocks(saleRep = saleRepository)

        whenever(saleRepository.getById(any())).doAnswer { flowOf() }

        sut.getSale(saleId = 1L)
        advanceUntilIdle()

        verify(saleRepository).getById(any())
    }

    @Test
    fun getSale_shouldSetSaleProperties() = runTest {
        val sale = makeRandomSale(id = 1L)
        saleRepository.insert(model = sale)

        sut.getSale(saleId = 1L)
        advanceUntilIdle()

        assertEquals(sale.name, sut.productName)
        assertEquals(sale.photo, sut.photo)
        assertEquals(sale.quantity.toString(), sut.quantity)
        assertEquals(sale.purchasePrice.toString(), sut.purchasePrice)
        assertEquals(sale.salePrice.toString(), sut.salePrice)
        assertEquals(sale.deliveryPrice.toString(), sut.deliveryPrice)
        assertEquals(sale.categories.joinToString(", ") { it.category }, sut.category)
        assertEquals(sale.company, sut.company)
        assertEquals(sale.isPaidByCustomer, sut.isPaidByCustomer)
    }

    @Test
    fun insertSale_shouldCallInsertItemsFromSaleRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = makeSutFromMocks(saleRep = saleRepository)

        sut.insertSale(isOrderedByCustomer = false, currentDate = 1000L, {}, {})
        advanceUntilIdle()

        verify(saleRepository).insertItems(any(), any(), any(), any(), any())
    }

    @Test
    fun updateSale_shouldCallUpdateFromSaleRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = makeSutFromMocks(saleRep = saleRepository)

        sut.updateSale(saleId = 1L)
        advanceUntilIdle()

        verify(saleRepository).update(any())
    }

    @Test
    fun deleteSale_shouldCallDeleteByIdFromSaleRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = makeSutFromMocks(saleRep = saleRepository)

        sut.deleteSale(saleId = 1L)
        advanceUntilIdle()

        verify(saleRepository).deleteById(any())
    }

    @Test
    fun cancelSale_shouldCallCancelSaleFromSaleRepository() = runTest {
        val saleRepository = mock<SaleRepository<Sale>>()
        val sut = makeSutFromMocks(saleRep = saleRepository)

        sut.cancelSale(saleId = 1L)
        advanceUntilIdle()

        verify(saleRepository).cancelSale(any())
    }

    private fun mapToCustomerCheck(customers: List<Customer>) = customers.map {
        CustomerCheck(
            id = it.id,
            name = it.name,
            address = it.address,
            phoneNumber = it.phoneNumber,
            isChecked = false
        )
    }

    private fun makeSutFromMocks(
        saleRep: SaleRepository<Sale> = mock(),
        stockOrderRep: StockOrderRepository<StockOrder> = mock(),
        productRep: ProductRepository<Product> = mock(),
        customerRep: CustomerRepository<Customer> = mock()
    ): SaleViewModel = SaleViewModel(saleRep, stockOrderRep, productRep, customerRep)
}