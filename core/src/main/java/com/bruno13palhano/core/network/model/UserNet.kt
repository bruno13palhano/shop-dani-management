package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json

data class UserNet(
    @Json(name = "uid") val uid: String,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "photo") val photo: String,
    @Json(name = "role") val role: String,
    @Json(name = "enabled") val enabled: Boolean,
    @Json(name = "timestamp") val timestamp: String
)