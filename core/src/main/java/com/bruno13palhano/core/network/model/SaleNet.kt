package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model

data class SaleNet(
    override val id: Long,
    val productId: Long,
    val stockId: Long,
    val customerId: Long,
    val quantity: Int,
    val purchasePrice: Float,
    val salePrice: Float,
    val deliveryPrice: Float,
    val amazonCode: String,
    val amazonRequestNumber: Long,
    val amazonTax: Int,
    val amazonProfit: Float,
    val amazonSKU: String,
    val resaleProfit: Float,
    val totalProfit: Float,
    val dateOfSale: Long,
    val dateOfPayment: Long,
    val shippingDate: Long,
    val deliveryDate: Long,
    val isOrderedByCustomer: Boolean,
    val isPaidByCustomer: Boolean,
    val delivered: Boolean,
    val canceled: Boolean,
    val isAmazon: Boolean,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp)