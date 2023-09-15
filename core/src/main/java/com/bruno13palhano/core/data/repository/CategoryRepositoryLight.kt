package com.bruno13palhano.core.data.repository

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
import javax.inject.Inject

class CategoryRepositoryLight @Inject constructor(
    private val categoryQueries:  CategoryTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CategoryData<Category> {
    override suspend fun insert(model: Category): Long {
        categoryQueries.insert(name = model.name)
        return 0L
    }

    override suspend fun update(model: Category) {
        categoryQueries.update(
            id = model.id,
            name = model.name
        )
    }

    override suspend fun delete(model: Category) {
        categoryQueries.delete(model.id)
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
    }

    override fun getLast(): Flow<Category> {
        return categoryQueries.getLast(mapper = ::mapCategory)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun search(value: String): Flow<List<Category>> {
        return categoryQueries.search(value = value, mapper = ::mapCategory)
            .asFlow().mapToList(ioDispatcher)
    }

    private fun mapCategory(
        id: Long,
        name: String
    ) = Category(
        id= id,
        name = name
    )
}