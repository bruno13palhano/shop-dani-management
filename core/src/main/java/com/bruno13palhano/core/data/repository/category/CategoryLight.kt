package com.bruno13palhano.core.data.repository.category

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CategoryTableQueries
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.OffsetDateTime
import javax.inject.Inject

internal class CategoryLight @Inject constructor(
    private val categoryQueries:  CategoryTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CategoryData<Category> {
    override suspend fun insert(model: Category): Long {
        if(model.id == 0L) {
            categoryQueries.insert(name = model.category, timestamp = model.timestamp.toString())
        } else {
            categoryQueries.insertWithId(
                id = model.id,
                name = model.category,
                timestamp = model.timestamp.toString()
            )
        }
        return categoryQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Category) {
        categoryQueries.update(
            id = model.id,
            name = model.category,
            timestamp = model.timestamp.toString()
        )
    }

    override suspend fun deleteById(id: Long) {
        categoryQueries.delete(id)
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
        time: String
    ): Category {
        return Category(
            id = id,
            category = name,
            timestamp = OffsetDateTime.parse(time)
        )
    }
}