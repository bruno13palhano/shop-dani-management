package com.bruno13palhano.core.data

import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow

/**
 * Contract for common data manipulation.
 *
 * An interface to decouple the treatment of common data, regardless of concrete implementation.
 * @param T the model type.
 */
interface DataOperations<T> {
    /**
     * Deletes the model of type [T] specified by this [id].
     * @param id the [id] of the model to be deleted.
     */
    suspend fun deleteById(
        id: Long,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )

    /**
     * Gets all models of type [T].
     * @return a [Flow] containing a [List] of all models of type [T].
     */
    fun getAll(): Flow<List<T>>

    /**
     * Gets the model of type [T] specified by this [id].
     * @param id the [id] for this model of type [T].
     * @return a [Flow] containing a model of type [T].
     */
    fun getById(id: Long): Flow<T>

    /**
     * Gets the last model of type [T] inserted.
     * @return a [Flow] containing a model of type [T].
     */
    fun getLast(): Flow<T>
}
