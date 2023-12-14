package com.bruno13palhano.core.data.repository.category

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CategoryTableQueries
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultCategoryData @Inject constructor(
    private val categoryQueries:  CategoryTableQueries,
    private val versionQueries: VersionTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CategoryData {
    override suspend fun insert(
        model: Category,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        var id = 0L

        try {
            if (model.isNew()) {
                categoryQueries.transaction {
                    categoryQueries.insert(
                        name = model.category,
                        timestamp = model.timestamp
                    )
                    id = categoryQueries.getLastId().executeAsOne()

                    versionQueries.insertWithId(
                        name = version.name,
                        timestamp = version.timestamp,
                        id = version.id
                    )

                    onSuccess(id)
                }
            } else {
                categoryQueries.transaction {
                    categoryQueries.insertWithId(
                        id = model.id,
                        name = model.category,
                        timestamp = model.timestamp
                    )

                    versionQueries.insertWithId(
                        name = version.name,
                        timestamp = version.timestamp,
                        id = version.id
                    )

                    id = model.id
                    onSuccess(model.id)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(Errors.INSERT_DATABASE_ERROR)
        }

        return id
    }

    override suspend fun update(
        model: Category,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            categoryQueries.transaction {
                categoryQueries.update(
                    id = model.id,
                    name = model.category,
                    timestamp = model.timestamp
                )

                versionQueries.update(
                    name = version.name,
                    timestamp = version.timestamp,
                    id = version.id
                )

                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(Errors.UPDATE_DATABASE_ERROR)
        }
    }

    override suspend fun deleteById(
        id: Long,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            categoryQueries.transaction {
                categoryQueries.delete(id)

                versionQueries.update(
                    name = version.name,
                    timestamp = version.timestamp,
                    id = version.id
                )

                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(Errors.DELETE_DATABASE_ERROR)
        }
    }

    override fun getAll(): Flow<List<Category>> {
        return categoryQueries.getAll(mapper = ::mapCategory)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Category> {
        return categoryQueries.getById(id = id, mapper = ::mapCategory)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Category> {
        return categoryQueries.getLast(mapper = ::mapCategory)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun search(value: String): Flow<List<Category>> {
        return categoryQueries.search(value = value, mapper = ::mapCategory)
            .asFlow().mapToList(ioDispatcher)
    }

    private fun mapCategory(
        id: Long,
        name: String,
        timestamp: String
    ): Category {
        return Category(
            id = id,
            category = name,
            timestamp = timestamp
        )
    }
}