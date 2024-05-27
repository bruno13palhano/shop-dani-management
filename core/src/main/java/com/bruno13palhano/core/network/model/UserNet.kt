package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class UserNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "photo") val photo: String,
    @Json(name = "role") val role: String,
    @Json(name = "enabled") val enabled: Boolean,
    @Json(name = "timestamp") override val timestamp: String
): Model(id = id, timestamp = timestamp)