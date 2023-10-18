package com.bruno13palhano.shopdanimanagement.delivery

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomDelivery
import com.bruno13palhano.shopdanimanagement.repository.TestDeliveryRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel.DeliveriesViewModel
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
class DeliveriesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var deliveryRepository: DeliveryData<Delivery>
    private lateinit var sut: DeliveriesViewModel
    private val deliveries = listOf(
        makeRandomDelivery(id = 1L, delivered = true),
        makeRandomDelivery(id = 2L, delivered = false),
        makeRandomDelivery(id = 3L, delivered = true)
    )

    @Before
    fun setup() {
        deliveryRepository = TestDeliveryRepository()
        sut = DeliveriesViewModel(deliveryRepository)
    }

    @Test
    fun getAllDeliveries_shouldCallGetAllFromRepository() = runTest {
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveriesViewModel(deliveryRepository)

        whenever(deliveryRepository.getAll()).doAnswer { flowOf() }

        sut.getAllDeliveries()
        advanceUntilIdle()

        verify(deliveryRepository).getAll()
    }

    @Test
    fun getAllDeliveries_shouldSetAllDeliveriesToDeliveriesProperty() = runTest {
        insertDeliveries()
        val collectJob = launch { sut.deliveries.collect() }

        sut.getAllDeliveries()
        advanceUntilIdle()

        assertEquals(mapToItem(deliveries), sut.deliveries.value)

        collectJob.cancel()
    }

    @Test
    fun getDeliveries_shouldCallGetDeliveriesFromRepository() = runTest {
        val deliveryRepository = mock<DeliveryData<Delivery>>()
        val sut = DeliveriesViewModel(deliveryRepository)

        whenever(deliveryRepository.getDeliveries(any())).doAnswer { flowOf() }

        sut.getDeliveries(delivered = true)
        advanceUntilIdle()

        verify(deliveryRepository).getDeliveries(any())
    }

    @Test
    fun getDeliveries_shouldSetDeliveriesToDeliveriesProperty() = runTest {
        insertDeliveries()
        val deliveredList = listOf(deliveries[0], deliveries[2])
        val collectJob = launch { sut.deliveries.collect() }

        sut.getDeliveries(delivered = true)
        advanceUntilIdle()

        assertEquals(mapToItem(deliveredList), sut.deliveries.value)

        collectJob.cancel()
    }

    private suspend fun insertDeliveries() = deliveries.forEach { deliveryRepository.insert(it) }

    private fun mapToItem(deliveries: List<Delivery>) = deliveries.map {
        CommonItem(
            id = it.id,
            title = it.customerName,
            photo = byteArrayOf(),
            subtitle = it.productName,
            description = it.address
        )
    }
}