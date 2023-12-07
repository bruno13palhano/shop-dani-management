package com.bruno13palhano.core.data.repository.stockorder

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface StockOrderRepository : DataOperations<StockOrder>, Syncable {
    fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>>
    fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>>
    fun getByCategory(category: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>>
    suspend fun updateStockOrderQuantity(id: Long, quantity: Int)
    fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>>
    fun getDebitStock(): Flow<List<StockOrder>>
    fun getOutOfStock(): Flow<List<StockOrder>>
    fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockOrder>>
    fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockOrder>>
}