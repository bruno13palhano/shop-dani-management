package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.network.CrudNetwork
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class CategoryNetworkRetrofit @Inject constructor(
    private val apiService: Service
): CrudNetwork<Category> {
    override suspend fun getAll(): List<Category> {
        return try {
            apiService.getAllCategories().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun insert(data: Category) {
        try {
            apiService.insertCategory(category = data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: Category) {
        try {
            apiService.updateCategory(category = data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteCategory(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}