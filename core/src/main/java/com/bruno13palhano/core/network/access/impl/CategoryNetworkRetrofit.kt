package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.model.CategoryNet
import javax.inject.Inject

internal class CategoryNetworkRetrofit
    @Inject
    constructor(
        private val apiService: Service,
    ) : CategoryNetwork {
        override suspend fun getAll(): List<CategoryNet> {
            return try {
                apiService.getAllCategories()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                emptyList()
            }
        }

        override suspend fun insert(data: CategoryNet) = apiService.insertCategory(category = data)

        override suspend fun update(data: CategoryNet) = apiService.updateCategory(category = data)

        override suspend fun delete(id: Long) = apiService.deleteCategory(id)
    }
