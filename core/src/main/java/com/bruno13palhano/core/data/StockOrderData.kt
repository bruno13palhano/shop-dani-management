package com.bruno13palhano.core.data

import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow

interface StockOrderData<T> : DataOperations<T> {
    suspend fun insertItems(stockOrder: StockOrder, isPaid: Boolean)
    fun getItems(isOrderedByCustomer: Boolean): Flow<List<T>>
    fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<T>>
    fun getByCategory(category: String, isOrderedByCustomer: Boolean): Flow<List<T>>
    suspend fun updateStockOrderQuantity(id: Long, quantity: Int)
    fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<T>>
    fun getDebitStock(): Flow<List<T>>
    fun getOutOfStock(): Flow<List<T>>
}