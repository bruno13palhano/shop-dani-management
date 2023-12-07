package com.bruno13palhano.shopdanimanagement.stockorder

import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomStockOrder
import com.bruno13palhano.shopdanimanagement.repository.TestProductRepository
import com.bruno13palhano.shopdanimanagement.repository.TestStockOrderRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.ItemViewModel
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
class ItemViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var productRepository: ProductRepository<Product>
    private lateinit var stockOrderRepository: StockOrderRepository<StockOrder>
    private lateinit var sut: ItemViewModel

    @Before
    fun setup() {
        productRepository = TestProductRepository()
        stockOrderRepository = TestStockOrderRepository()
        sut = ItemViewModel(productRepository, stockOrderRepository)
    }

    @Test
    fun isItemNotEmpty_shouldReturnTrue_whenPropertiesIsNotEmpty() = runTest {
        sut.updateQuantity(quantity = "10")
        sut.updatePurchasePrice(purchasePrice = "11.20")
        sut.updateSalePrice(salePrice = "12.30")

        val collectJob = launch { sut.isItemNotEmpty.collect() }
        advanceUntilIdle()

        assertEquals(true, sut.isItemNotEmpty.value)

        collectJob.cancel()
    }

    @Test
    fun isItemNotEmpty_shouldReturnFalse_whenPropertiesIsEmpty() = runTest {
        sut.updateQuantity(quantity = "")
        sut.updatePurchasePrice(purchasePrice = "11.20")
        sut.updateSalePrice(salePrice = "12.30")

        val collectJob = launch { sut.isItemNotEmpty.collect() }
        advanceUntilIdle()

        assertEquals(false, sut.isItemNotEmpty.value)

        collectJob.cancel()
    }

    @Test
    fun updateQuantity_shouldChangeQuantity() {
        val quantity = "10"
        sut.updateQuantity(quantity = quantity)

        assertEquals(quantity, sut.quantity)
    }

    @Test
    fun updateDate_shouldChangeDate() {
        val date = 2000L
        sut.updateDate(date = date)

        assertEquals(date, sut.date)
    }

    @Test
    fun updateValidity_shouldChangeValidity() {
        val validity = 3000L
        sut.updateValidity(validity = validity)

        assertEquals(validity, sut.validity)
    }

    @Test
    fun updatePurchasePrice_shouldChangePurchasePrice() {
        val purchasePrice = "123.00"
        sut.updatePurchasePrice(purchasePrice = purchasePrice)

        assertEquals(purchasePrice, sut.purchasePrice)
    }

    @Test
    fun updateSalePrice_shouldChangeSalePrice() {
        val salePrice = "321.00"
        sut.updateSalePrice(salePrice = salePrice)

        assertEquals(salePrice, sut.salePrice)
    }

    @Test
    fun updateIsPaid_shouldChangeIsPaid() {
        val isPaid = true
        sut.updateIsPaid(isPaid = isPaid)

        assertEquals(isPaid, sut.isPaid)
    }

    @Test
    fun getProduct_shouldCallGetByIdFromProductRepository() = runTest {
        val productRepository = mock<ProductRepository<Product>>()
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val sut = ItemViewModel(productRepository, stockOrderRepository)

        whenever(productRepository.getById(any())).doAnswer { flowOf() }

        sut.getProduct(id = 1L)
        advanceUntilIdle()

        verify(productRepository).getById(any())
    }

    @Test
    fun getProduct_shouldSetItemPropertiesThatDependOnProduct() = runTest {
        val product = makeRandomProduct(id = 1L)
        productRepository.insert(model = product)

        sut.getProduct(id = product.id)
        advanceUntilIdle()

        assertEquals(product.name, sut.name)
        assertEquals(product.photo, sut.photo)
        assertEquals(product.company, sut.company)
        assertEquals(product.date, sut.date)
        assertEquals(product.categories.joinToString(", ") { it.category }, sut.category)
    }

    @Test
    fun getStockOrder_shouldSetStockOrderProperties() = runTest {
        val stockOrder = makeRandomStockOrder(id = 1L)
        stockOrderRepository.insert(model = stockOrder)

        sut.getStockOrder(stockOrderItemId = stockOrder.id)
        advanceUntilIdle()

        assertEquals(stockOrder.name, sut.name)
        assertEquals(stockOrder.photo, sut.photo)
        assertEquals(stockOrder.quantity.toString(), sut.quantity)
        assertEquals(stockOrder.company, sut.company)
        assertEquals(stockOrder.date, sut.date)
        assertEquals(stockOrder.validity, sut.validity)
        assertEquals(stockOrder.categories.joinToString(", ") { it.category }, sut.category)
        assertEquals(stockOrder.purchasePrice.toString(), sut.purchasePrice)
        assertEquals(stockOrder.salePrice.toString(), sut.salePrice)
        assertEquals(stockOrder.isPaid, sut.isPaid)
    }

    @Test
    fun getStockOrder_shouldCallGetByIdFromStockOrderRepository() = runTest {
        val productRepository = mock<ProductRepository<Product>>()
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val sut = ItemViewModel(productRepository, stockOrderRepository)

        whenever(stockOrderRepository.getById(any())).doAnswer { flowOf() }

        sut.getStockOrder(stockOrderItemId = 1L)
        advanceUntilIdle()

        verify(stockOrderRepository).getById(any())
    }

    @Test
    fun insertItems_shouldCallInsertFromStockOrderRepository() = runTest {
        val productRepository = mock<ProductRepository<Product>>()
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val sut = ItemViewModel(productRepository, stockOrderRepository)

        sut.insertItems(productId = 1L, isOrderedByCustomer = true)
        advanceUntilIdle()

        verify(stockOrderRepository).insert(any())
    }

    @Test
    fun updateStockOrderItem_shouldCallUpdateFromStockOrderRepository() = runTest {
        val productRepository = mock<ProductRepository<Product>>()
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val sut = ItemViewModel(productRepository, stockOrderRepository)

        sut.updateStockOrderItem(stockOrderItemId = 1L, isOrderedByCustomer = true)
        advanceUntilIdle()

        verify(stockOrderRepository).update(any())
    }

    @Test
    fun deleteStockOrderItem_shouldCallDeleteByIdFromStockOrderRepository() = runTest {
        val productRepository = mock<ProductRepository<Product>>()
        val stockOrderRepository = mock<StockOrderRepository<StockOrder>>()
        val sut = ItemViewModel(productRepository, stockOrderRepository)

        sut.deleteStockOrderItem(stockOrderId = 1L)
        advanceUntilIdle()

        verify(stockOrderRepository).deleteById(any())
    }
}