package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.database.dao.ShoppingDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.Shopping
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ShoppingCacheRepository @Inject constructor(
    private val shoppingDao: ShoppingDao
) : ShoppingData<Shopping> {
    override suspend fun insert(model: Shopping): Long {
        return shoppingDao.insert(model.asInternalModel())
    }

    override suspend fun update(model: Shopping) {
        shoppingDao.update(model.asInternalModel())
    }

    override suspend fun delete(model: Shopping) {
        shoppingDao.delete(model.asInternalModel())
    }

    override suspend fun deleteById(id: Long) {
        shoppingDao.deleteById(id)
    }

    override fun getAll(): Flow<List<Shopping>> {
        return shoppingDao.getAll()
            .map {
                it.map { entity -> entity.asExternalModel()}
            }
    }

    override fun getById(id: Long): Flow<Shopping> {
        return shoppingDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Shopping> {
        return shoppingDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getItemsLimited(offset: Int, limit: Int): Flow<List<Shopping>> {
        return shoppingDao.getItemsLimited(offset, limit)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }
}