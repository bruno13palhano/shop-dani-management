package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestCategoryRepository : CategoryRepository<Category> {
    private val categories = mutableListOf<Category>()

    override fun search(value: String): Flow<List<Category>> {
        return flowOf(categories).map {
            it.filter { categoryName -> categoryName.category == value }
        }
    }

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = categories)
        if (isIndexValid(index = index)) categories.removeAt(index)
    }

    override fun getAll(): Flow<List<Category>> = flowOf(categories)

    override fun getById(id: Long): Flow<Category> {
        val index = getIndex(id = id, list = categories)
        return if (isIndexValid(index = index)) flowOf(categories[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Category> = flowOf(categories.last())

    override suspend fun update(model: Category) {
        val index = getIndex(id = model.id, list = categories)
        if (isIndexValid(index = index)) categories[index] = model
    }

    override suspend fun insert(model: Category): Long {
        categories.add(model)
        return model.id
    }
}