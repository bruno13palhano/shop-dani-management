package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.di.InternalCategoryLight
import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CategoryRepository @Inject constructor(
    @InternalCategoryLight private val categoryData: CategoryData<Category>
): CategoryData<Category> {
    override fun search(value: String): Flow<List<Category>> {
        return categoryData.search(value = value)
    }

    override suspend fun deleteById(id: Long) {
        categoryData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Category>> {
        return categoryData.getAll()
    }

    override fun getById(id: Long): Flow<Category> {
        return categoryData.getById(id = id)
    }

    override fun getLast(): Flow<Category> {
        return categoryData.getLast()
    }

    override suspend fun update(model: Category) {
        return categoryData.update(model = model)
    }

    override suspend fun insert(model: Category): Long {
        return categoryData.insert(model = model)
    }
}