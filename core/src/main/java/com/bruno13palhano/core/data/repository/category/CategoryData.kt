package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow

interface CategoryData : DataOperations<Category> {
    suspend fun insert(
        model: Category,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long
    suspend fun update(
        model: Category,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    )
    fun search(value: String): Flow<List<Category>>
}