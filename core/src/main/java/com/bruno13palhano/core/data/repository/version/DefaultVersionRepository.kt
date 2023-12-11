package com.bruno13palhano.core.data.repository.version

import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultVersionRepository @Inject constructor(
    @InternalVersionLight private val versionData: VersionData
) : VersionRepository {
    override suspend fun insert(
        model: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        return versionData.insert(model = model, {}) {}
    }

    override suspend fun update(
        model: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        versionData.update(model = model, {}) {}
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        versionData.deleteById(id = id, {}) {}
    }

    override fun getAll(): Flow<List<DataVersion>> {
        return versionData.getAll()
    }

    override fun getById(id: Long): Flow<DataVersion> {
        return versionData.getById(id = id)
    }

    override fun getLast(): Flow<DataVersion> {
        return versionData.getLast()
    }
}