package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface ProductData<T> : DataOperations<T> {

    /**
     * Searches for products of type [T] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the products referring to the search.
     */
    fun search(value: String): Flow<List<T>>
}