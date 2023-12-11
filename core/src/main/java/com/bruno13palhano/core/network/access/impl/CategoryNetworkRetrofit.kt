package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.CategoryNetwork
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

    override suspend fun insert(data: Category) =
        apiService.insertCategory(category = data.asNetwork())

    override suspend fun update(data: Category) =
        apiService.updateCategory(category = data.asNetwork())

    override suspend fun delete(id: Long) = apiService.deleteCategory(id)
}