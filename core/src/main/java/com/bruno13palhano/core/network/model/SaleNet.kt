package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class SaleNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "stockId") val stockId: Long,
    @Json(name = "customerId") val customerId: Long,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "purchasePrice") val purchasePrice: Float,
    @Json(name = "salePrice") val salePrice: Float,
    @Json(name = "deliveryPrice") val deliveryPrice: Float,
    @Json(name = "amazonCode") val amazonCode: String,
    @Json(name = "amazonRequestNumber") val amazonRequestNumber: Long,
    @Json(name = "amazonTax") val amazonTax: Int,
    @Json(name = "amazonProfit") val amazonProfit: Float,
    @Json(name = "amazonSKU") val amazonSKU: String,
    @Json(name = "resaleProfit") val resaleProfit: Float,
    @Json(name = "totalProfit") val totalProfit: Float,
    @Json(name = "dateOfSale") val dateOfSale: Long,
    @Json(name = "dateOfPayment") val dateOfPayment: Long,
    @Json(name = "shippingDate") val shippingDate: Long,
    @Json(name = "deliveryDate") val deliveryDate: Long,
    @Json(name = "isOrderedByCustomer") val isOrderedByCustomer: Boolean,
    @Json(name = "isPaidByCustomer") val isPaidByCustomer: Boolean,
    @Json(name = "delivered") val delivered: Boolean,
    @Json(name = "canceled") val canceled: Boolean,
    @Json(name = "isAmazon") val isAmazon: Boolean,
    @Json(name = "timestamp") override val timestamp: String
) : Model(id = id, timestamp = timestamp)