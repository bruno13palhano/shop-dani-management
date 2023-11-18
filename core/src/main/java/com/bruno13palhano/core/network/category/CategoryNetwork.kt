package com.bruno13palhano.core.network.category

import com.bruno13palhano.core.model.Category

internal interface CategoryNetwork {
    suspend fun getAll(): List<Category>
}