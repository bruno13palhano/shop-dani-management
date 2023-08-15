package com.bruno13palhano.core.model

data class Shopping(
    val id: Long,
    val productId: Long,
    val name: String,
    val purchasePrice: Float,
    val quantity: Int,
    val date: Long,
    val isPaid: Boolean
)
