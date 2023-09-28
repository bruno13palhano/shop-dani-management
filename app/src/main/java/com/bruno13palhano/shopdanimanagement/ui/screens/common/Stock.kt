package com.bruno13palhano.shopdanimanagement.ui.screens.common

data class Stock(
    val id: Long,
    val name: String,
    val photo: String,
    val purchasePrice: Float,
    val quantity: Int,
)