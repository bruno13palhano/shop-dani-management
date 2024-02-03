package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductData : DataOperations<Product> {
    suspend fun insert(
        model: Product,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long
    suspend fun update(
        model: Product,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    )
    fun search(value: String): Flow<List<Product>>
    fun searchPerCategory(value: String ,categoryId: Long): Flow<List<Product>>
    fun getByCategory(category: String): Flow<List<Product>>
    fun getByCode(code: String): Flow<List<Product>>
}