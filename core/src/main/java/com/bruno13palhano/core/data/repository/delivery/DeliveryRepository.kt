package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.model.Delivery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DeliveryRepository @Inject constructor(
    @InternalDeliveryLight private val deliveryData: DeliveryData<Delivery>
) : DeliveryData<Delivery> {
    override suspend fun insert(model: Delivery): Long {
        return deliveryData.insert(model = model)
    }

    override suspend fun update(model: Delivery) {
        deliveryData.update(model = model)
    }

    override suspend fun delete(model: Delivery) {
        deliveryData.delete(model = model)
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
        return deliveryData.getAll()
    }

    override fun getById(id: Long): Flow<Delivery> {
        return deliveryData.getById(id = id)
    }

    override fun getLast(): Flow<Delivery> {
        return deliveryData.getLast()
    }
}