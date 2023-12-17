package com.bruno13palhano.shopdanimanagement.delivery

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomSale
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
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

    private lateinit var saleRepository: SaleRepository
    private lateinit var sut: DeliveriesViewModel
    private val sales = listOf(
        makeRandomSale(id = 1L, canceled = false, delivered = true),
        makeRandomSale(id = 2L, canceled = false, delivered = false),
        makeRandomSale(id = 3L, canceled = false, delivered = true)
    )

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = DeliveriesViewModel(saleRepository)
    }

    @Test
    fun getAllDeliveries_shouldCallGetAllFromRepository() = runTest {
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveriesViewModel(saleRepository)

        whenever(saleRepository.getAll()).doAnswer { flowOf() }

        sut.getAllDeliveries()
        advanceUntilIdle()

        verify(saleRepository).getAll()
    }

    @Test
    fun getAllDeliveries_shouldSetAllDeliveriesToDeliveriesProperty() = runTest {
        insertDeliveries()
        val collectJob = launch { sut.deliveries.collect() }

        sut.getAllDeliveries()
        advanceUntilIdle()

        assertEquals(sales.map { sale ->
            Delivery(
                id = sale.id,
                saleId = sale.id,
                customerName = sale.customerName,
                address = sale.address,
                phoneNumber = sale.phoneNumber,
                productName = sale.name,
                price = sale.salePrice,
                deliveryPrice = sale.deliveryPrice,
                shippingDate = sale.shippingDate,
                deliveryDate = sale.deliveryDate,
                delivered = sale.delivered,
                timestamp = sale.timestamp
            )
        }, sut.deliveries.value)

        collectJob.cancel()
    }

    @Test
    fun getDeliveries_shouldCallGetDeliveriesFromRepository() = runTest {
        val saleRepository = mock<SaleRepository>()
        val sut = DeliveriesViewModel(saleRepository)

        whenever(saleRepository.getDeliveries(any())).doAnswer { flowOf() }

        sut.getDeliveries(delivered = true)
        advanceUntilIdle()

        verify(saleRepository).getDeliveries(any())
    }

    @Test
    fun getDeliveries_shouldSetDeliveriesToDeliveriesProperty() = runTest {
        insertDeliveries()
        val deliveredList = listOf(sales[0], sales[2])
        val collectJob = launch { sut.deliveries.collect() }

        sut.getDeliveries(delivered = true)
        advanceUntilIdle()

        assertEquals(deliveredList.map { sale ->
            Delivery(
                id = sale.id,
                saleId = sale.id,
                customerName = sale.customerName,
                address = sale.address,
                phoneNumber = sale.phoneNumber,
                productName = sale.name,
                price = sale.salePrice,
                deliveryPrice = sale.deliveryPrice,
                shippingDate = sale.shippingDate,
                deliveryDate = sale.deliveryDate,
                delivered = sale.delivered,
                timestamp = sale.timestamp
            )
        }, sut.deliveries.value)

        collectJob.cancel()
    }

    private suspend fun insertDeliveries() = sales.forEach { saleRepository.insert(it, {}, {}) }
}