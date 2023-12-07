package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository : DataOperations<Delivery>, Syncable {
    suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float)
    suspend fun updateShippingDate(id: Long, shippingDate: Long)
    suspend fun updateDeliveryDate(id: Long, deliveryDate: Long)
    suspend fun updateDelivered(id: Long, delivered: Boolean)
    fun getDeliveries(delivered: Boolean): Flow<List<Delivery>>
    fun getCanceledDeliveries(): Flow<List<Delivery>>
}