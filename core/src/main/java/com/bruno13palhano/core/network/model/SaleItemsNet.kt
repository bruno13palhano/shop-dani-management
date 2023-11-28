package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaleItemsNet(
    @Json(name = "sale") var sale: SaleNet,
    @Json(name = "stockOrder") var stockOrder: StockOrderNet,
    @Json(name = "delivery") var delivery: DeliveryNet
)
