package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json

data class StockOrderNet(
    @Json val id: Long,
    @Json val productId: Long,
    @Json val date: Long,
    @Json val validity: Long,
    @Json val purchasePrice: Float,
    @Json val salePrice: Float,
    @Json val isOrderedByCustomer: Boolean,
    @Json val isPaid: Boolean
)
