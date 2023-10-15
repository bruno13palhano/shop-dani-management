package com.bruno13palhano.shopdanimanagement.products.repository

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.makeRandomCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestCategoryRepository :  CategoryData<Category> {
    val categories = mutableListOf(
        makeRandomCategory(id = 1L),
        makeRandomCategory(id = 2L),
        makeRandomCategory(id = 3L)
    )

    override fun search(value: String): Flow<List<Category>> {
        return flowOf(categories).map {
            it.filter { categoryName -> categoryName.name == value }
        }
    }

    override suspend fun deleteById(id: Long) {
        var index = 1000
        for(i in 0 .. categories.size) {
            if (categories[i].id == id)
                index = i
        }

        if (index < 1000)
            categories.removeAt(index)
    }

    override fun getAll(): Flow<List<Category>> {
        return flowOf(categories)
    }

    override fun getById(id: Long): Flow<Category> {
        var index = 1000
        for(i in 0 .. categories.size) {
            if (categories[i].id == id)
                index = i
        }

        return flowOf(categories[index])
    }

    override fun getLast(): Flow<Category> {
        return flowOf(categories.last())
    }

    override suspend fun update(model: Category) {
        var index = 1000
        for(i in 0 .. categories.size) {
            if (categories[i].id == model.id)
                index = i
        }

        categories[index] = model
    }

    override suspend fun insert(model: Category): Long {
        categories.add(model)
        return model.id
    }
}