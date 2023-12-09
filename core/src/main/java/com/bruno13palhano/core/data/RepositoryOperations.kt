package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface RepositoryOperations<T> {
    suspend fun insert(model: T): Long
    suspend fun update(model: T)
    suspend fun deleteById(id: Long, timestamp: String)
    fun getAll(): Flow<List<T>>
    fun getById(id: Long): Flow<T>
    fun getLast(): Flow<T>
}