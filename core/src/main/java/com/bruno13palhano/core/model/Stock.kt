package com.bruno13palhano.core.model

data class Stock(
    val id: Long,
    val name: String,
    val purchasePrice: Float,
    val quantity: Int,
)