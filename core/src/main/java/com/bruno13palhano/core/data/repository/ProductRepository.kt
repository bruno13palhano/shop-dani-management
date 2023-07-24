package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.database.dao.ProductDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) : DataOperations<Product> {
    override suspend fun insert(model: Product): Long {
        return productDao.insert(model.asInternalModel())
    }

    override suspend fun update(model: Product) {
        productDao.update(model.asInternalModel())
    }

    override suspend fun delete(model: Product) {
        productDao.delete(model.asInternalModel())
    }

    override suspend fun deleteById(id: Long) {
        productDao.deleteById(id)
    }

    override fun getAll(): Flow<List<Product>> {
        return productDao.getAll()
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getById(id: Long): Flow<Product> {
        return productDao.getById(id)
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Product> {
        return productDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }
}