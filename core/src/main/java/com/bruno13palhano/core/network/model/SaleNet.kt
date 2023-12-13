package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Sale
import com.squareup.moshi.Json

data class SaleNet (
    @Json(name = "id") val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "stockId") val stockId: Long,
    @Json(name = "customerId") val customerId: Long,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "purchasePrice") val purchasePrice: Float,
    @Json(name = "salePrice") val salePrice: Float,
    @Json(name = "deliveryPrice") val deliveryPrice: Float,
    @Json(name = "dateOfSale") val dateOfSale: Long,
    @Json(name = "dateOfPayment") val dateOfPayment: Long,
    @Json(name = "shippingDate") val shippingDate: Long,
    @Json(name = "deliveryDate") val deliveryDate: Long,
    @Json(name = "isOrderedByCustomer") val isOrderedByCustomer: Boolean,
    @Json(name = "isPaidByCustomer") val isPaidByCustomer: Boolean,
    @Json(name = "delivered") val delivered: Boolean,
    @Json(name = "canceled") val canceled: Boolean,
    @Json(name = "timestamp") val timestamp: String
)

internal fun SaleNet.asExternal() = Sale(
    id = id,
    productId = productId,
    stockId = stockId,
    customerId = customerId,
    name = "",
    customerName = "",
    photo = byteArrayOf(),
    address = "",
    phoneNumber = "",
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    deliveryPrice = deliveryPrice,
    categories = listOf(),
    company = "",
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaidByCustomer = isPaidByCustomer,
    delivered = delivered,
    canceled = canceled,
    timestamp = timestamp
)

internal fun Sale.asNetwork() = SaleNet(
    id = id,
    productId = productId,
    stockId = stockId,
    customerId = customerId,
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    deliveryPrice = deliveryPrice,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaidByCustomer = isPaidByCustomer,
    delivered = delivered,
    canceled = canceled,
    timestamp = timestamp
)