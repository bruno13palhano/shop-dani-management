package com.bruno13palhano.core.data.repository.version

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultVersionData @Inject constructor(
    private val versionQueries: VersionTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : VersionData {
    override suspend fun insert(model: DataVersion, onSuccess: (id: Long) -> Unit): Long {
        if (model.id == 0L) {
            versionQueries.insert(
                name = model.name,
                timestamp = model.timestamp,
            )
            val id = versionQueries.getLastId().executeAsOne()
            onSuccess(id)

            return id
        } else {
            versionQueries.insertWithId(
                id = model.id,
                name = model.name,
                timestamp = model.timestamp
            )
            onSuccess(model.id)

            return model.id
        }
    }

    override suspend fun update(model: DataVersion, onSuccess: () -> Unit) {
        versionQueries.update(
            name = model.name,
            timestamp = model.timestamp,
            id = model.id
        )
        onSuccess()
    }

    override suspend fun deleteById(id: Long, onSuccess: () -> Unit) {
        versionQueries.delete(id = id)
        onSuccess()
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
        return versionQueries.getLast(mapper = ::mapVersion)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    private fun mapVersion(
        id: Long,
        name: String,
        timestamp: String
    ) = DataVersion(
        id = id,
        name = name,
        timestamp = timestamp
    )
}