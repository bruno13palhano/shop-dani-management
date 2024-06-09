package com.bruno13palhano.core.data.repository.version

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class LocalVersionData
    @Inject
    constructor(
        private val versionQueries: VersionTableQueries,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : VersionData {
        override suspend fun insert(
            model: DataVersion,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit
        ): Long {
            try {
                if (model.isNew()) {
                    versionQueries.insert(
                        name = model.name,
                        timestamp = model.timestamp
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
            } catch (e: Exception) {
                e.printStackTrace()
                onError(1)

                return 0L
            }
        }

        override suspend fun update(
            model: DataVersion,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                versionQueries.update(
                    name = model.name,
                    timestamp = model.timestamp,
                    id = model.id
                )
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(2)
            }
        }

        override suspend fun deleteById(
            id: Long,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                versionQueries.delete(id = id)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(3)
            }
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