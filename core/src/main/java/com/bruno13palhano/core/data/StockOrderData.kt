package com.bruno13palhano.core.data

import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface StockOrderData<T> : DataOperations<T>, Syncable {
    fun getItems(isOrderedByCustomer: Boolean): Flow<List<T>>
    fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<T>>
    fun getByCategory(category: String, isOrderedByCustomer: Boolean): Flow<List<T>>
    suspend fun updateStockOrderQuantity(id: Long, quantity: Int)
    fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<T>>
    fun getDebitStock(): Flow<List<T>>
    fun getOutOfStock(): Flow<List<T>>
    fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<T>>
    fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<T>>
}