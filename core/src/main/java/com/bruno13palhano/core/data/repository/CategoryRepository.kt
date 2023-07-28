package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.database.dao.CategoryDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : DataOperations<Category> {
    override suspend fun insert(model: Category): Long {
        return categoryDao.insert(model.asInternalModel())
    }

    override suspend fun update(model: Category) {
        categoryDao.update(model.asInternalModel())
    }

    override suspend fun delete(model: Category) {
        categoryDao.delete(model.asInternalModel())
    }

    override suspend fun deleteById(id: Long) {
        categoryDao.deleteById(id)
    }

    override fun getAll(): Flow<List<Category>> {
        return categoryDao.getAll().map { entities ->
            entities.map { it.asExternalModel() }
        }
    }

    override fun getById(id: Long): Flow<Category> {
        return categoryDao.getById(id)
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Category> {
        return categoryDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }
}