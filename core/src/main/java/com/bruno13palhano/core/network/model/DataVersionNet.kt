package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class DataVersionNet(
    @Json(name = "id") override val id: Long = 0L,
    @Json(name = "name") val name: String = "",
    @Json(name = "timestamp") override val timestamp: String = "1974-03-03T00:00:00Z"
) : Model(id = id, timestamp = timestamp)