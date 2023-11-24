package com.bruno13palhano.core.network.category

import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class CategoryNetworkRetrofit @Inject constructor(
    private val apiService: Service
): CategoryNetwork {
    override suspend fun getAll(): List<Category> {
        return try {
            apiService.getAllCategories().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun insertCategory(category: Category) {
        try {
            apiService.insertCategory(category.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun updateCategory(category: Category) {
        try {
            apiService.updateCategory(category.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun deleteCategory(id: Long) {
        try {
            apiService.deleteCategory(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}