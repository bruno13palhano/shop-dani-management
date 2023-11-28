package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.network.access.DeliveryNetwork
import com.bruno13palhano.core.network.di.DefaultDeliveryNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DeliveryRepository @Inject constructor(
    @DefaultDeliveryNet private val deliveryNetwork: DeliveryNetwork,
    @InternalDeliveryLight private val deliveryData: DeliveryData<Delivery>,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DeliveryData<Delivery> {
    override suspend fun insert(model: Delivery): Long {
        CoroutineScope(ioDispatcher).launch {
            deliveryNetwork.insert(model)
        }
        return deliveryData.insert(model = model)
    }

    override suspend fun update(model: Delivery) {
        deliveryData.update(model = model)
    }

    override suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float) {
        deliveryData.updateDeliveryPrice(id = id, deliveryPrice = deliveryPrice)
    }

    override suspend fun updateShippingDate(id: Long, shippingDate: Long) {
        deliveryData.updateShippingDate(id = id, shippingDate = shippingDate)
    }

    override suspend fun updateDeliveryDate(id: Long, deliveryDate: Long) {
        deliveryData.updateDeliveryDate(id = id, deliveryDate = deliveryDate)
    }

    override suspend fun updateDelivered(id: Long, delivered: Boolean) {
        deliveryData.updateDelivered(id = id, delivered = delivered)
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Delivery>> {
        return deliveryData.getDeliveries(delivered = delivered)
    }

    override suspend fun deleteById(id: Long) {
        deliveryData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Delivery>> {
        CoroutineScope(ioDispatcher).launch {
            deliveryNetwork.getAll().forEach { deliveryData.insert(it) }
        }
        return deliveryData.getAll()
    }

    override fun getById(id: Long): Flow<Delivery> {
        return deliveryData.getById(id = id)
    }

    override fun getLast(): Flow<Delivery> {
        return deliveryData.getLast()
    }

    override fun getCanceledDeliveries(): Flow<List<Delivery>> {
        return deliveryData.getCanceledDeliveries()
    }
}