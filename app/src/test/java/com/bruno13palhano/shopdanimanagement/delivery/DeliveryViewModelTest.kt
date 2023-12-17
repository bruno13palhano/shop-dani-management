package com.bruno13palhano.shopdanimanagement.delivery

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel.DeliveryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class DeliveryViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleRepository
    private lateinit var sut: DeliveryViewModel

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = DeliveryViewModel(saleRepository)
    }

    @Test
    fun updateDeliveryPrice_shouldChangeDeliveryPrice() {
        val deliveryPrice = "12.90"
        sut.updateDeliveryPrice(deliveryPrice = deliveryPrice)

        assertEquals(deliveryPrice, sut.deliveryPrice)
    }

    @Test
    fun updateShippingDate_shouldChangeShippingDate() {
        val shippingDate = 10000L
        sut.updateShippingDate(shippingDate = shippingDate)

        assertEquals(shippingDate, sut.shippingDate)
    }

    @Test
    fun updateDeliveryDate_shouldChangeDeliveryDate() {
        val deliveryDate = 10000L
        sut.updateDeliveryDate(deliveryDate = deliveryDate)

        assertEquals(deliveryDate, sut.deliveryDate)
    }

    @Test
    fun updateDelivered_shouldChangeDelivered() {
        val delivered = false
        sut.updateDelivered(delivered = delivered)

        assertEquals(delivered, sut.delivered)
    }

    @Test
    fun getDeliveryById_shouldCallGetByIdFromRepository() = runTest {
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveryViewModel(saleRepository)

        whenever(saleRepository.getById(any())).doAnswer { flowOf() }

        sut.getDeliveryById(saleId = 1L)
        advanceUntilIdle()

        verify(saleRepository).getById(any())
    }

    @Test
    fun getDeliveryById_shouldSetDeliveryProperties() = runTest {
        val sales = listOf(makeRandomSale(id = 1L), makeRandomSale(id = 2L))
        val current = sales[1]
        sales.forEach { saleRepository.insert(it, {}, {}) }

        sut.getDeliveryById(saleId = current.id)
        advanceUntilIdle()

        assertEquals(current.customerName, sut.name)
        assertEquals(current.address, sut.address)
        assertEquals(current.phoneNumber, sut.phoneNumber)
        assertEquals(current.name, sut.productName)
        assertEquals(current.salePrice.toString(), sut.price)
        assertEquals(current.deliveryPrice.toString(), sut.deliveryPrice)
        assertEquals(current.shippingDate, sut.shippingDate)
        assertEquals(current.deliveryDate, sut.deliveryDate)
        assertEquals(current.delivered, sut.delivered)
    }

    @Test
    fun updateDelivery_shouldCallUpdateDeliveryPriceFromRepository() = runTest {
        val deliveryPrice = "123"
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveryViewModel(saleRepository)

        sut.updateDeliveryPrice(deliveryPrice)
        sut.updateDelivery(saleId = 1L, {}, {})
        advanceUntilIdle()

        verify(saleRepository).update(any(), any(), any())
    }

    @Test
    fun updateDelivery_shouldCallUpdateShippingDateFromRepository() = runTest {
        val shippingDate = 2000L
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveryViewModel(saleRepository)

        sut.updateShippingDate(shippingDate)
        sut.updateDelivery(saleId = 1L, {}, {})
        advanceUntilIdle()

        verify(saleRepository).update(any(), any(), any())
    }

    @Test
    fun updateDelivery_shouldCallUpdateDeliveryDateFromRepository() = runTest {
        val deliveryDate = 2000L
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveryViewModel(saleRepository)

        sut.updateDeliveryDate(deliveryDate)
        sut.updateDelivery(saleId = 1L, {}, {})
        advanceUntilIdle()

        verify(saleRepository).update(any(), any(), any())
    }

    @Test
    fun updateDelivery_shouldCallUpdateDeliveredFromRepository() = runTest {
        val delivered = true
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveryViewModel(saleRepository)

        sut.updateDelivered(delivered)
        sut.updateDelivery(saleId = 1L, {}, {})
        advanceUntilIdle()

        verify(saleRepository).update(any(), any(), any())
    }
}