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
     * Gets all products of type [T] by category.
     * @param category the category to search.
     * @param isOrderedByCustomer defines which products to searching, stocked or ordered.
     * @return a [Flow] containing a [List] of all product of type [T] by category.
     */
    fun getProductsByCategory(category: String, isOrderedByCustomer: Boolean): Flow<List<T>>

    /**
     * Searches for products of type [T] with this value.
     * @param search the searching value.
     * @param isOrderedByCustomer defines which products to searching, stocked or ordered.
     * @return a [Flow] containing a [List] of all products of type [T] referring to the search.
     */
    fun searchProduct(search: String, isOrderedByCustomer: Boolean): Flow<List<T>>

    /**
     * Searches for products of type [T] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the products referring to the search.
     */
    fun search(value: String): Flow<List<T>>
}