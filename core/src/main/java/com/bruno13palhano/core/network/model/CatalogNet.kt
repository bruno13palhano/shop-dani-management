package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class CatalogNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "discount") val discount: Long,
    @Json(name = "price") val price: Float,
    @Json(name = "timestamp") override val timestamp: String
) : Model(id = id, timestamp = timestamp)