package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Category
import com.squareup.moshi.Json

data class ProductCategoriesNet(
    @Json val id: Long,
    @Json val categories: List<Category>
)
