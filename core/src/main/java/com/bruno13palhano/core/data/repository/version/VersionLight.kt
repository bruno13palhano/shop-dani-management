package com.bruno13palhano.core.data.repository.version

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.VersionTableQueries
import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.OffsetDateTime
import javax.inject.Inject

internal class VersionLight @Inject constructor(
    private val versionQueries: VersionTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : VersionData<DataVersion>{
    override suspend fun insert(model: DataVersion): Long {
        if (model.id == 0L) {
            versionQueries.insert(
                name = model.name,
                timestamp = model.timestamp.toString(),
            )
        } else {
            versionQueries.insertWithId(
                id = model.id,
                name = model.name,
                timestamp = model.timestamp.toString()
            )
        }

        return 1L
    }

    override suspend fun update(model: DataVersion) {
        versionQueries.update(
            name = model.name,
            timestamp = model.timestamp.toString(),
            id = model.id
        )
    }

    override suspend fun deleteById(id: Long) {
        versionQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<DataVersion>> {
        return versionQueries.getAll(mapper = ::mapVersion).asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<DataVersion> {
        return versionQueries.getVersion(id = id, mapper = ::mapVersion)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<DataVersion> {
        TODO("Not yet implemented")
    }

    private fun mapVersion(
        id: Long,
        name: String,
        timestamp: String
    ) = DataVersion(
        id = id,
        name = name,
        timestamp = OffsetDateTime.parse(timestamp)
    )
}