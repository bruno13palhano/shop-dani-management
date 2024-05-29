package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface RepositoryOperations<T> {
    suspend fun insert(
        model: T,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit,
    ): Long

    suspend fun update(
        model: T,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )

    suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )

    fun getAll(): Flow<List<T>>

    fun getById(id: Long): Flow<T>

    fun getLast(): Flow<T>
}
