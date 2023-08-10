package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface ProductData<T> : DataOperations<T> {
    /**
     * Gets all products of type [T] in stock.
     * @return a [Flow] containing a [List] of all products of type [T] in stock.
     */
    fun getAllStockProducts(): Flow<List<T>>

    /**
     * Gets all product of type [T] ordered by customers.
     * @return a [Flow] containing a [List] of all products o f type [T] ordered by customers.
     */
    fun getAllOrderedProducts(): Flow<List<T>>

    /**
     * Searches for products of type [T] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the products referring to the search.
     */
    fun search(value: String): Flow<List<T>>
}