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
     * Deletes the model of type [T] specified by this [id].
     * @param id the [id] of the model to be deleted.
     */
    suspend fun deleteById(id: Long)

    /**
     * Gets all models of type [T].
     * @return a [Flow] containing a [List] of all models of type [T].
     */
    fun getAll(): Flow<List<T>>
}