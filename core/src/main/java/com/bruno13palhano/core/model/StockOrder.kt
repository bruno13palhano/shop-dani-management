package com.bruno13palhano.core.model

data class StockOrder(
    val id: Long,
    val productId: Long,
    val name: String,
    val photo: String,
    val date: Long,
    val validity: Long,
    val quantity: Int,
    val categories: List<String>,
    val company: String,
    val purchasePrice: Float,
    val salePrice: Float,
    val isOrderedByCustomer: Boolean
)
