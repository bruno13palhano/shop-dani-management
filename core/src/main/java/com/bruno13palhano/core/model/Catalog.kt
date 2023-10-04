package com.bruno13palhano.core.model

data class Catalog(
    val id: Long,
    val productId: Long,
    val name: String,
    val photo: ByteArray,
    val title: String,
    val description: String,
    val discount: Long,
    val price: Float
)
