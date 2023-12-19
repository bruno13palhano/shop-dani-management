package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.User
import com.squareup.moshi.Json
import java.util.Base64

data class UserNet(
    @Json(name = "id") val id: Long,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "photo") val photo: String,
    @Json(name = "role") val role: String,
    @Json(name = "enabled") val enabled: Boolean,
    @Json(name = "timestamp") val timestamp: String
)

fun UserNet.asExternal() = User(
    id = id,
    username = username,
    email = email,
    password = password,
    photo = Base64.getDecoder().decode(photo),
    role = role,
    enabled = enabled,
    timestamp = timestamp
)

fun User.asNetwork() = UserNet(
    id = id,
    username = username,
    email = email,
    password = password,
    photo = Base64.getEncoder().encodeToString(photo),
    role = role,
    enabled = enabled,
    timestamp = timestamp
)
