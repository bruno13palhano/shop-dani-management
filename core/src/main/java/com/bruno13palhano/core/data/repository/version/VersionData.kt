package com.bruno13palhano.core.data.repository.version

import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow

interface VersionData {
    suspend fun insert(
        model: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long

    suspend fun update(model: DataVersion, onError: (error: Int) -> Unit, onSuccess: () -> Unit)

    suspend fun deleteById(id: Long, onError: (error: Int) -> Unit, onSuccess: () -> Unit)

    fun getAll(): Flow<List<DataVersion>>

    fun getById(id: Long): Flow<DataVersion>

    fun getLast(): Flow<DataVersion>
}