package com.bruno13palhano.core.data.repository.stockorder

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.StockItem
import kotlinx.coroutines.flow.Flow

interface StockData : DataOperations<StockItem> {
    fun getItems(): Flow<List<StockItem>>
    fun search(value: String): Flow<List<StockItem>>
    fun getByCategory(category: String): Flow<List<StockItem>>
    suspend fun updateStockOrderQuantity(id: Long, quantity: Int)
    fun getStockOrderItems(): Flow<List<StockItem>>
    fun getDebitStock(): Flow<List<StockItem>>
    fun getOutOfStock(): Flow<List<StockItem>>
    fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>>
    fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>>
}