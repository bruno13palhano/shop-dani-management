package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.database.dao.StockOrderDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class StockOrderRepository @Inject constructor(
    private val stockOrderDao: StockOrderDao
) : StockOrderData<StockOrder> {
    override suspend fun insert(model: StockOrder): Long {
        return stockOrderDao.insert(model.asInternalModel())
    }

    override suspend fun update(model: StockOrder) {
        stockOrderDao.update(model.asInternalModel())
    }

    override suspend fun delete(model: StockOrder) {
        stockOrderDao.delete(model.asInternalModel())
    }

    override suspend fun deleteById(id: Long) {
        stockOrderDao.deleteById(id)
    }

    override fun getAll(): Flow<List<StockOrder>> {
        return stockOrderDao.getAll()
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderDao.getById(id)
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderDao.getItems(isOrderedByCustomer)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun search(value: String): Flow<List<StockOrder>> {
        return stockOrderDao.search(value)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getByCategory(category: String): Flow<List<StockOrder>> {
        return stockOrderDao.getByCategory(category)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        return stockOrderDao.updateStockOrderQuantity(id, quantity)
    }
}