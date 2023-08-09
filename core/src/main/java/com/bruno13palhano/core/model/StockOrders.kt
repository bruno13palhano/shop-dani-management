package com.bruno13palhano.core.model

data class StockOrders(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val isOrderedByCustomer: Boolean
)
