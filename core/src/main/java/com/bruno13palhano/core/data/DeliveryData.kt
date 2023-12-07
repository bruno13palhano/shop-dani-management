package com.bruno13palhano.core.data

import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface DeliveryData<T> : DataOperations<T>, Syncable {
    suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float)
    suspend fun updateShippingDate(id: Long, shippingDate: Long)
    suspend fun updateDeliveryDate(id: Long, deliveryDate: Long)
    suspend fun updateDelivered(id: Long, delivered: Boolean)
    fun getDeliveries(delivered: Boolean): Flow<List<T>>
    fun getCanceledDeliveries(): Flow<List<T>>
}