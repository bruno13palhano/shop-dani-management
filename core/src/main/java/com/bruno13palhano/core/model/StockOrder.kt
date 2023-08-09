package com.bruno13palhano.core.model

data class StockOrder(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val isOrderedByCustomer: Boolean
)
