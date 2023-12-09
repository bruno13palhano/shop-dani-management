package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.DataVersion
import com.squareup.moshi.Json

data class DataVersionNet(
    @Json(name = "id") val id: Long = 0L,
    @Json(name = "name") val name: String = "",
    @Json(name = "timestamp") val timestamp: String = "1974-03-03T00:00:00Z",
)

internal fun DataVersionNet.asExternal() = DataVersion(
    id = id,
    name = name,
    timestamp = timestamp
)

internal fun DataVersion.asNetwork() = DataVersionNet(
    id = id,
    name = name,
    timestamp = timestamp
)