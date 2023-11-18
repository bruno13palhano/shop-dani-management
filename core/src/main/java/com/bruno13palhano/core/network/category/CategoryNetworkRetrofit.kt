package com.bruno13palhano.core.network.category

import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.model.asExternal
import javax.inject.Inject

internal class CategoryNetworkRetrofit @Inject constructor(
    private val apiService: Service
): CategoryNetwork {
    override suspend fun getAll(): List<Category> {
        return try {
            apiService.getAllCategories().map { it.asExternal() }
        } catch (ignored: Exception) { emptyList() }
    }
}