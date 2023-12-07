package com.bruno13palhano.core.data

import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface CategoryData<T> : DataOperations<T>, Syncable {
    /**
     * Searches for categories of type [T] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the categories referring to the search.
     */
    fun search(value: String): Flow<List<T>>
}