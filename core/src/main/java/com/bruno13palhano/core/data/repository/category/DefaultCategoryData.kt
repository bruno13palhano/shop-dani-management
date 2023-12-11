package com.bruno13palhano.core.data.repository.category

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CategoryTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultCategoryData @Inject constructor(
    private val categoryQueries:  CategoryTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CategoryData {
    override suspend fun insert(
        model: Category,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        try {
            if (model.isNew()) {
                categoryQueries.insert(
                    name = model.category,
                    timestamp = model.timestamp
                )
                val id = categoryQueries.getLastId().executeAsOne()
                onSuccess(id)

                return id
            } else {
                categoryQueries.insertWithId(
                    id = model.id,
                    name = model.category,
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
        model: Category,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            categoryQueries.update(
                id = model.id,
                name = model.category,
                timestamp = model.timestamp
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
            categoryQueries.delete(id)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(3)
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