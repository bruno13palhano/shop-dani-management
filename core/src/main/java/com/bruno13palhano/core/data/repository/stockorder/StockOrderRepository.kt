package com.bruno13palhano.core.data.repository.stockorder

import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class StockOrderRepository @Inject constructor(
    @InternalStockOrderLight private val stockOrderData: StockOrderData<StockOrder>
) : StockOrderData<StockOrder> {
    override suspend fun insert(model: StockOrder): Long {
        return stockOrderData.insert(model = model)
    }

    override suspend fun update(model: StockOrder) {
        stockOrderData.update(model = model)
    }

    override suspend fun delete(model: StockOrder) {
        stockOrderData.delete(model = model)
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getItems(isOrderedByCustomer = isOrderedByCustomer)
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.search(value = value, isOrderedByCustomer = isOrderedByCustomer)
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return stockOrderData.getByCategory(
            category = category,
            isOrderedByCustomer = isOrderedByCustomer
        )
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        stockOrderData.updateStockOrderQuantity(id = id, quantity = quantity)
    }

    override suspend fun deleteById(id: Long) {
        stockOrderData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<StockOrder>> {
        return stockOrderData.getAll()
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderData.getById(id = id)
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderData.getLast()
    }
}