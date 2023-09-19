package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.database.dao.SaleDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.Sale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SaleRoom @Inject constructor(
    private val saleDao: SaleDao
) : InternalSaleData {
    override suspend fun insert(model: Sale): Long {
        return saleDao.insert(model.asInternalModel())
    }

    override suspend fun update(model: Sale) {
        saleDao.update(model.asInternalModel())
    }

    override suspend fun delete(model: Sale) {
        saleDao.delete(model.asInternalModel())
    }

    override suspend fun deleteById(id: Long) {
        saleDao.deleteById(id)
    }

    override fun getAll(): Flow<List<Sale>> {
        return saleDao.getAll()
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleDao.getById(id)
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Sale> {
        return saleDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleDao.getByCustomerId(customerId)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleDao.getLastSales(offset, limit)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleDao.getAllStockSales(offset, limit)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleDao.getAllOrdersSales(offset, limit)
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }
}