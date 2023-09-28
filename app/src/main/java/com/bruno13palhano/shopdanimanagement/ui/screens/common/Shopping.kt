package com.bruno13palhano.shopdanimanagement.ui.screens.common

data class Shopping(
    val id: Long,
    val stockItemId: Long,
    val name: String,
    val photo: String,
    val purchasePrice: Float,
    val quantity: Int,
    val date: Long,
    val isPaid: Boolean
)