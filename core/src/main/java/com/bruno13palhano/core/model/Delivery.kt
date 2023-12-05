package com.bruno13palhano.core.model

import java.time.OffsetDateTime

data class Delivery(
    override val id: Long,
    val saleId: Long,
    val customerName: String,
    val address: String,
    val phoneNumber: String,
    val productName: String,
    val price: Float,
    val deliveryPrice: Float,
    val shippingDate: Long,
    val deliveryDate: Long,
    val delivered: Boolean,
    override val timestamp: OffsetDateTime
) : Model(id = id, timestamp = timestamp)