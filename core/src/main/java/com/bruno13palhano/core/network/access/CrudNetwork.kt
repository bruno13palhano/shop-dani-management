package com.bruno13palhano.core.network.access

interface CrudNetwork<T> {
    suspend fun getAll(): List<T>
    suspend fun insert(data: T)
    suspend fun update(data: T)
    suspend fun delete(id: Long)
}