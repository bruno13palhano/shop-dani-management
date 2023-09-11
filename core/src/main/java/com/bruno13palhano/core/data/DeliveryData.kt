package com.bruno13palhano.core.data

interface DeliveryData<T> : DataOperations<T> {
    suspend fun updateShippingDate(id: Long, shippingDate: Long)
    suspend fun updateDeliveryDate(id: Long, deliveryDate: Long)
    suspend fun updateDelivered(id: Long, delivered: Boolean)
}