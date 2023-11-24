package com.bruno13palhano.core.network.category

import com.bruno13palhano.core.model.Category

internal interface CategoryNetwork {
    suspend fun getAll(): List<Category>
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(id: Long)
}