package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json

data class DeliveryNet(
    @Json val id: Long,
    @Json val saleId: Long,
    @Json val deliveryPrice: Float,
    @Json val shippingDate: Long,
    @Json val deliveryDate: Long,
    @Json val delivered: Boolean
)
