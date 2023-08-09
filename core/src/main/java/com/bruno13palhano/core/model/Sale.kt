package com.bruno13palhano.core.model

data class Sale(
    val id: Long,
    val productId: Long,
    val dateOfSale: Long,
    val dateOfPayment: Long,
    val isPaidByClient: Boolean
)
