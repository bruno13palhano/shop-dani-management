package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryData : DataOperations<Category> {
    fun search(value: String): Flow<List<Category>>
}