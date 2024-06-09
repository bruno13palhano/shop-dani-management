package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class CategoryNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "category") val category: String,
    @Json(name = "timestamp") override val timestamp: String
) : Model(id = id, timestamp = timestamp)