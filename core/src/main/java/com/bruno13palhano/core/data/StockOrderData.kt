package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface StockOrderData<T> : DataOperations<T> {
    fun getItems(isOrderedByCustomer: Boolean): Flow<List<T>>
    fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<T>>
    fun getByCategory(category: String, isOrderedByCustomer: Boolean): Flow<List<T>>
    suspend fun updateStockOrderQuantity(id: Long, quantity: Int)
}