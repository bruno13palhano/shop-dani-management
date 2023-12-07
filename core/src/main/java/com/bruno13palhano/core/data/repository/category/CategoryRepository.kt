package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface CategoryRepository : DataOperations<Category>, Syncable {
    /**
     * Searches for categories of type [T] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the categories referring to the search.
     */
    fun search(value: String): Flow<List<Category>>
}