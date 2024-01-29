package com.bruno13palhano.core.data.repository.stock

import com.bruno13palhano.core.data.RepositoryOperations
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface StockRepository : RepositoryOperations<StockItem>, Syncable {
    fun getItems(): Flow<List<StockItem>>
    fun search(value: String): Flow<List<StockItem>>
    fun getByCategory(category: String): Flow<List<StockItem>>
    fun getByCode(code: String): Flow<List<StockItem>>
    suspend fun updateStockQuantity(id: Long, quantity: Int, timestamp: String)
    fun getStockItems(): Flow<List<StockItem>>
    fun getDebitStock(): Flow<List<StockItem>>
    fun getOutOfStock(): Flow<List<StockItem>>
    fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>>
    fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>>
}