package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestCategoryRepository :  CategoryData<Category> {
    private val categories = mutableListOf<Category>()

    override fun search(value: String): Flow<List<Category>> {
        return flowOf(categories).map {
            it.filter { categoryName -> categoryName.name == value }
        }
    }

    override suspend fun deleteById(id: Long) {
        var index = -1
        for(i in 0 until categories.size) {
            if (categories[i].id == id)
                index = i
        }

        if (index != -1)
            categories.removeAt(index)
    }

    override fun getAll(): Flow<List<Category>> {
        return flowOf(categories)
    }

    override fun getById(id: Long): Flow<Category> {
        var index = -1
        for(i in 0 until categories.size) {
            if (categories[i].id == id)
                index = i
        }

        return if (index != -1) flowOf(categories[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Category> {
        return flowOf(categories.last())
    }

    override suspend fun update(model: Category) {
        var index = -1
        for(i in 0 until categories.size) {
            if (categories[i].id == model.id)
                index = i
        }

        if (index != -1)
            categories[index] = model
    }

    override suspend fun insert(model: Category): Long {
        categories.add(model)
        return model.id
    }
}