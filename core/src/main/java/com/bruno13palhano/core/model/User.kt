package com.bruno13palhano.core.model

data class User(
    val uid: String,
    val username: String,
    val email: String,
    val password: String,
    val photo: String,
    val timestamp: String
)