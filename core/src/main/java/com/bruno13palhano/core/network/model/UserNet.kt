package com.bruno13palhano.core.network.model

data class UserNet(
    val uid: String,
    val username: String,
    val email: String,
    val password: String,
    val photo: String,
    val timestamp: String
)