package com.bruno13palhano.core.data

import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface ProductData<T> : DataOperations<T>, Syncable {

    /**
     * Searches for products of type [T] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the products referring to the search.
     */
    fun search(value: String): Flow<List<T>>

    fun searchPerCategory(value: String ,categoryId: Long): Flow<List<T>>

    /**
     * Gets products by category.
     * @param category the category name.
     * @return a [Flow] containing a [List] of all the products referring to the category.
     */
    fun getByCategory(category: String): Flow<List<T>>
}