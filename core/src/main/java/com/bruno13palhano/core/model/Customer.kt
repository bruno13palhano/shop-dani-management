package com.bruno13palhano.core.model

data class Customer(
    val id: Long,
    val name: String,
    val photo: String,
    val email: String,
    val address: String,
    val phoneNumber: String,
)