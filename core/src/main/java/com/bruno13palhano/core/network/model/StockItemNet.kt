package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model

data class StockItemNet(
    override val id: Long,
    val productId: Long,
    val date: Long,
    val dateOfPayment: Long,
    val validity: Long,
    val quantity: Int,
    val purchasePrice: Float,
    val salePrice: Float,
    val isPaid: Boolean,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp)