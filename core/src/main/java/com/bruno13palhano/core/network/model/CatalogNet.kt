package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Catalog
import com.squareup.moshi.Json

data class CatalogNet(
    @Json(name = "id") val id: Long,
    @Json(name = "productId") val productId: Long,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "discount") val discount: Long,
    @Json(name = "price") val price: Float
)

internal fun CatalogNet.asExternal() = Catalog(
    id = id,
    productId = productId,
    name = "",
    photo = byteArrayOf(),
    title = title,
    description = description,
    discount = discount,
    price = price
)

internal fun Catalog.asNetwork() = CatalogNet(
    id = id,
    productId = productId,
    title = title,
    description = description,
    discount = discount,
    price = price
)