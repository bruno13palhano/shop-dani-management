package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Delivery
import com.squareup.moshi.Json

data class DeliveryNet(
    @Json(name = "id") val id: Long,
    @Json(name = "saleId") val saleId: Long,
    @Json(name = "deliveryPrice") val deliveryPrice: Float,
    @Json(name = "shippingDate") val shippingDate: Long,
    @Json(name = "deliveryDate") val deliveryDate: Long,
    @Json(name = "delivered") val delivered: Boolean
)

internal fun DeliveryNet.asExternal() = Delivery(
    id = id,
    saleId = saleId,
    customerName = "",
    address = "",
    phoneNumber = "",
    productName = "",
    price = 0F,
    deliveryPrice = deliveryPrice,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    delivered = delivered
)

internal fun Delivery.asNetwork() = DeliveryNet(
    id = id,
    saleId = saleId,
    deliveryPrice = deliveryPrice,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    delivered = delivered
)