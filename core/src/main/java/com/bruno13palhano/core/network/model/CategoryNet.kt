package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Category
import com.squareup.moshi.Json
import java.time.OffsetDateTime

data class CategoryNet(
    @Json(name = "id") val id: Long,
    @Json(name = "category") val category: String,
    @Json(name = "timestamp") val timestamp: String
)

internal fun CategoryNet.asExternal() = Category(
    id = id,
    category = category,
    timestamp = OffsetDateTime.parse(timestamp)
)

internal fun Category.asNetwork() = CategoryNet(
    id = id,
    category = category,
    timestamp = timestamp.toString()
)
