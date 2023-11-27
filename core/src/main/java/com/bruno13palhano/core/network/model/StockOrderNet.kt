package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.StockOrder
import com.squareup.moshi.Json
import java.time.OffsetDateTime

data class StockOrderNet(
    @Json(name = "id") val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "date") val date: Long,
    @Json(name = "validity") val validity: Long,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "purchasePrice") val purchasePrice: Float,
    @Json(name = "salePrice") val salePrice: Float,
    @Json(name = "isOrderedByCustomer") val isOrderedByCustomer: Boolean,
    @Json(name = "isPaid") val isPaid: Boolean,
    @Json(name = "timestamp") val timestamp: String
)

internal fun StockOrderNet.asExternal() = StockOrder(
    id = id,
    productId = productId,
    name = "",
    photo = byteArrayOf(),
    date = date,
    validity = validity,
    quantity = quantity,
    categories = listOf(),
    company = "",
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaid = isPaid,
    timestamp = OffsetDateTime.parse(timestamp)
)

internal fun StockOrder.asNetwork() = StockOrderNet(
    id = id,
    productId = productId,
    date = date,
    validity = validity,
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaid = isPaid,
    timestamp = timestamp.toString()
)