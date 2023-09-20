package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.model.Sale
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SaleRepository @Inject constructor(
    @InternalSaleLight private val saleData: InternalSaleData
) : SaleData<Sale> {
    override suspend fun insert(model: Sale): Long {
        return saleData.insert(model = model)
    }

    override suspend fun update(model: Sale) {
        saleData.update(model = model)
    }

    override suspend fun delete(model: Sale) {
        saleData.delete(model = model)
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleData.getByCustomerId(customerId = customerId)
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getLastSales(offset = offset, limit = limit)
    }

    override fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getAllStockSales(offset = offset, limit = limit)
    }

    override fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getAllOrdersSales(offset = offset, limit = limit)
    }

    override suspend fun deleteById(id: Long) {
        saleData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Sale>> {
        return saleData.getAll()
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleData.getById(id = id)
    }

    override fun getLast(): Flow<Sale> {
        return saleData.getLast()
    }
}