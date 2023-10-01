package com.bruno13palhano.core.model

data class StockOrder(
    val id: Long,
    val productId: Long,
    val name: String,
    val photo: ByteArray,
    val date: Long,
    val validity: Long,
    val quantity: Int,
    val categories: List<Category>,
    val company: String,
    val purchasePrice: Float,
    val salePrice: Float,
    val isOrderedByCustomer: Boolean,
    val isPaid: Boolean
)
