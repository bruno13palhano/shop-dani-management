package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SaleRepository @Inject constructor(
    @InternalSaleLight private val saleData: SaleData<Sale>
) : SaleData<Sale> {
    override suspend fun insert(model: Sale): Long {
        return saleData.insert(model = model)
    }

    override suspend fun update(model: Sale) {
        saleData.update(model = model)
    }

    override suspend fun setCanceledSale(saleId: Long, stockOrderId: Long, canceled: Boolean) {
        saleData.setCanceledSale(saleId = saleId, stockOrderId = stockOrderId, canceled = canceled)
    }

    override suspend fun delete(model: Sale) {
        saleData.delete(model = model)
    }

    override suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        saleData.insertItems(
            sale = sale,
            stockOrder = stockOrder,
            delivery = delivery,
            onSuccess = onSuccess,
            onError = onError
        )
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

    override fun getCanceledSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getCanceledSales(offset = offset, limit = limit)
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleData.getById(id = id)
    }

    override fun getLast(): Flow<Sale> {
        return saleData.getLast()
    }
}