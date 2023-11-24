package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Category
import com.squareup.moshi.Json

data class ProductCategoriesNet(
    @Json(name = "id") val id: Long,
    @Json(name = "categories") val categories: List<Category>
)
