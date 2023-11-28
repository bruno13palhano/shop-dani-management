package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Sale
import com.squareup.moshi.Json
import java.time.OffsetDateTime

data class SaleNet (
    @Json(name = "id") val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "stockOrderId") val stockOrderId: Long,
    @Json(name = "customerId") val customerId: Long,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "purchasePrice") val purchasePrice: Float,
    @Json(name = "salePrice") val salePrice: Float,
    @Json(name = "dateOfSale") val dateOfSale: Long,
    @Json(name = "dateOfPayment") val dateOfPayment: Long,
    @Json(name = "isOrderedByCustomer") val isOrderedByCustomer: Boolean,
    @Json(name = "isPaidByCustomer") val isPaidByCustomer: Boolean,
    @Json(name = "canceled") val canceled: Boolean,
    @Json(name = "timestamp") val timestamp: String
)

internal fun SaleNet.asExternal() = Sale(
    id = id,
    productId = productId,
    stockOrderId = stockOrderId,
    customerId = customerId,
    name = "",
    customerName = "",
    photo = byteArrayOf(),
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    deliveryPrice = 0F,
    categories = listOf(),
    company = "",
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaidByCustomer = isPaidByCustomer,
    canceled = canceled,
    timestamp = OffsetDateTime.parse(timestamp)
)

internal fun Sale.asNetwork() = SaleNet(
    id = id,
    productId = productId,
    stockOrderId = stockOrderId,
    customerId = customerId,
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaidByCustomer = isPaidByCustomer,
    canceled = canceled,
    timestamp = timestamp.toString()
)