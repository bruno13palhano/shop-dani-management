package com.bruno13palhano.core.model

data class Sale(
    val id: Long,
    val productId: Long,
    val name: String,
    val photo: String,
    val quantity: Int,
    val purchasePrice: Float,
    val salePrice: Float,
    val categories: List<String>,
    val company: String,
    val dateOfSale: Long,
    val dateOfPayment: Long,
    val isOrderedByCustomer: Boolean,
    val isPaidByCustomer: Boolean
)
