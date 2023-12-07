package com.bruno13palhano.core.data.repository.version

import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class VersionRepository @Inject constructor(
    @InternalVersionLight private val versionLight: VersionData<DataVersion>
) : VersionData<DataVersion> {
    override suspend fun insert(model: DataVersion): Long {
        return versionLight.insert(model = model)
    }

    override suspend fun update(model: DataVersion) {
        versionLight.update(model = model)
    }

    override suspend fun deleteById(id: Long) {
        versionLight.deleteById(id = id)
    }

    override fun getAll(): Flow<List<DataVersion>> {
        return versionLight.getAll()
    }

    override fun getById(id: Long): Flow<DataVersion> {
        return versionLight.getById(id = id)
    }

    override fun getLast(): Flow<DataVersion> {
        return versionLight.getLast()
    }
}