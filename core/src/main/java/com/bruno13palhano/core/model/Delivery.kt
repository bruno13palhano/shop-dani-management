package com.bruno13palhano.core.model

data class Delivery(
    val id: Long,
    val saleId: Long,
    val customerName: String,
    val address: String,
    val phoneNumber: String,
    val productName: String,
    val price: Float,
    val delivered: Boolean,
)