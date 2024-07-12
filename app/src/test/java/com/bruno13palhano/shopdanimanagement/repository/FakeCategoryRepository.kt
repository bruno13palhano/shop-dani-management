package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeCategoryRepository : CategoryRepository {
    private val categories = mutableListOf<Category>()

    override fun search(value: String): Flow<List<Category>> {
        return flowOf(categories).map {
            it.filter { categoryName -> categoryName.category == value }
        }
    }

    override suspend fun insert(
        model: Category,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        categories.add(model)
        onSuccess(model.id)
        return model.id
    }

    override suspend fun update(
        model: Category,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = model.id, list = categories)
        if (isIndexValid(index = index)) {
            categories[index] = model
            onSuccess()
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = id, list = categories)
        if (isIndexValid(index = index)) {
            categories.removeAt(index)
            onSuccess()
        }
    }

    override fun getAll(): Flow<List<Category>> = flowOf(categories)

    override fun getById(id: Long): Flow<Category> {
        val index = getIndex(id = id, list = categories)
        return if (isIndexValid(index = index)) flowOf(categories[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Category> = flowOf(categories.last())

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}