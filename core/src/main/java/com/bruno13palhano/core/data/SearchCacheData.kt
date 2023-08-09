package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

/**
 * Contract for SearchCache data manipulation.
 *
 * A common interface to handling search cache data manipulation.
 * @param T the model type.
 */
interface SearchCacheData<T> {
    /**
     * Insert a [model] of type [T].
     * @param model the new [model] to be inserted.
     * @return the id of the new [model].
     */
    suspend fun insert(model: T): Long

    /**
     * Deletes the search of type [T] specified by this [search].
     * @param search the [search] value of the search to be deleted.
     */
    suspend fun deleteById(search: String)

    /**
     * Gets all search of type [T].
     * @return a [Flow] containing a [List] of all search of type [T].
     */
    fun getAll(): Flow<List<T>>

    /**
     * Gets specific search of type [T].
     * @param isOrderedByCustomer defines where to perform the search, stocked or ordered.
     * @return a [Flow] containing a [List] of all search of type [T].
     */
    fun getSearchCache(isOrderedByCustomer: Boolean): Flow<List<T>>
}