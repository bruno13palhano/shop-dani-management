package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface DeliveryData<T> : DataOperations<T> {
    suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float)
    suspend fun updateShippingDate(id: Long, shippingDate: Long)
    suspend fun updateDeliveryDate(id: Long, deliveryDate: Long)
    suspend fun updateDelivered(id: Long, delivered: Boolean)
    fun getDeliveries(delivered: Boolean): Flow<List<T>>
}