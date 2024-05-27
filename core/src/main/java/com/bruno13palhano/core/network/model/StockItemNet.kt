package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class StockItemNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "date") val date: Long,
    @Json(name = "dateOfPayment") val dateOfPayment: Long,
    @Json(name = "validity") val validity: Long,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "purchasePrice") val purchasePrice: Float,
    @Json(name = "salePrice") val salePrice: Float,
    @Json(name = "isPaid") val isPaid: Boolean,
    @Json(name = "timestamp") override val timestamp: String
): Model(id = id, timestamp = timestamp)