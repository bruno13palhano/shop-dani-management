package com.bruno13palhano.core.model

data class Receipt(
    val id: Long,
    val productId: Long,
    val stockId: Long,
    val customerId: Long,
    val quantity: Int,
    val purchasePrice: Float,
    val salePrice: Float,
    val deliveryPrice: Float,
    val dateOfSale: Long,
    val dateOfPayment: Long,
    val shippingDate: Long,
    val deliveryDate: Long,
    val ordered: Boolean,
    val paid: Boolean,
    val delivered: Boolean,
    val cancelled: Boolean,
    val timestamp: String
)