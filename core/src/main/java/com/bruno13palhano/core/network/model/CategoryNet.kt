package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Category
import com.squareup.moshi.Json

data class CategoryNet(
    @Json(name = "id") val id: Long,
    @Json(name = "category") val name: String
)

internal fun CategoryNet.asExternal() = Category(
    id = id,
    name = name
)

internal fun Category.asNetwork() = CategoryNet(
    id = id,
    name = name
)
