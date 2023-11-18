package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json

data class CatalogNet(
    @Json val id: Long,
    @Json val productId: Long,
    @Json val title: String,
    @Json val description: String,
    @Json val discount: Long,
    @Json val price: Float
)
