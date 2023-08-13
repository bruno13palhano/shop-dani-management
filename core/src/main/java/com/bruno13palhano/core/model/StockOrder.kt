package com.bruno13palhano.core.model

data class StockOrder(
    val id: Long,
    val productId: Long,
    val name: String,
    val photo: String,
    val purchasePrice: Float,
    val date: Long,
    val quantity: Int,
    val isOrderedByCustomer: Boolean
)
