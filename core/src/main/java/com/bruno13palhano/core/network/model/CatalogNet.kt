package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model

data class CatalogNet(
    override val id: Long,
    val productId: Long,
    val title: String,
    val description: String,
    val discount: Long,
    val price: Float,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp)