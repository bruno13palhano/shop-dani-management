package com.bruno13palhano.shopdanimanagement.delivery

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomDelivery
import com.bruno13palhano.shopdanimanagement.repository.TestDeliveryRepository
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

    private lateinit var deliveryRepository: DeliveryData<Delivery>
    private lateinit var sut: DeliveryViewModel

    @Before
    fun setup() {
        deliveryRepository = TestDeliveryRepository()
        sut = DeliveryViewModel(deliveryRepository)
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
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveryViewModel(deliveryRepository)

        whenever(deliveryRepository.getById(any())).doAnswer { flowOf() }

        sut.getDeliveryById(deliveryId = 1L)
        advanceUntilIdle()

        verify(deliveryRepository).getById(any())
    }

    @Test
    fun getDeliveryById_shouldSetDeliveryProperties() = runTest {
        val deliveries = listOf(makeRandomDelivery(id = 1L), makeRandomDelivery(id = 2L))
        val current = deliveries[1]
        deliveries.forEach { deliveryRepository.insert(it) }

        sut.getDeliveryById(deliveryId = current.id)
        advanceUntilIdle()

        assertEquals(current.customerName, sut.name)
        assertEquals(current.address, sut.address)
        assertEquals(current.phoneNumber, sut.phoneNumber)
        assertEquals(current.productName, sut.productName)
        assertEquals(current.price.toString(), sut.price)
        assertEquals(current.deliveryPrice.toString(), sut.deliveryPrice)
        assertEquals(current.shippingDate, sut.shippingDate)
        assertEquals(current.deliveryDate, sut.deliveryDate)
        assertEquals(current.delivered, sut.delivered)
    }

    @Test
    fun updateDelivery_shouldCallUpdateDeliveryPriceFromRepository() = runTest {
        val deliveryPrice = "123"
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveryViewModel(deliveryRepository)

        sut.updateDeliveryPrice(deliveryPrice)
        sut.updateDelivery(deliveryId = 1L)
        advanceUntilIdle()

        verify(deliveryRepository).updateDeliveryPrice(any(), any())
    }

    @Test
    fun updateDelivery_shouldCallUpdateShippingDateFromRepository() = runTest {
        val shippingDate = 2000L
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveryViewModel(deliveryRepository)

        sut.updateShippingDate(shippingDate)
        sut.updateDelivery(deliveryId = 1L)
        advanceUntilIdle()

        verify(deliveryRepository).updateShippingDate(any(), any())
    }

    @Test
    fun updateDelivery_shouldCallUpdateDeliveryDateFromRepository() = runTest {
        val deliveryDate = 2000L
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveryViewModel(deliveryRepository)

        sut.updateDeliveryDate(deliveryDate)
        sut.updateDelivery(deliveryId = 1L)
        advanceUntilIdle()

        verify(deliveryRepository).updateDeliveryDate(any(), any())
    }

    @Test
    fun updateDelivery_shouldCallUpdateDeliveredFromRepository() = runTest {
        val delivered = true
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveryViewModel(deliveryRepository)

        sut.updateDelivered(delivered)
        sut.updateDelivery(deliveryId = 1L)
        advanceUntilIdle()

        verify(deliveryRepository).updateDelivered(any(), any())
    }
}