package com.bruno13palhano.core.data.repository.stock

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.StockItem
import kotlinx.coroutines.flow.Flow

interface StockData : DataOperations<StockItem> {
    suspend fun insert(
        model: StockItem,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long
    suspend fun update(
        model: StockItem,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    )
    fun getItems(): Flow<List<StockItem>>
    fun getByCode(code: String): Flow<List<StockItem>>
    fun search(value: String): Flow<List<StockItem>>
    fun getByCategory(category: String): Flow<List<StockItem>>
    suspend fun updateStockQuantity(id: Long, quantity: Int)
    fun getStockItems(): Flow<List<StockItem>>
    fun getDebitStock(): Flow<List<StockItem>>
    fun getOutOfStock(): Flow<List<StockItem>>
    fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>>
    fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>>
}