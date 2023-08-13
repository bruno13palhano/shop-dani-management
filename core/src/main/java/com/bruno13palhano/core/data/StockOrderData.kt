package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface StockOrderData<T> : DataOperations<T> {
    fun search(value: String): Flow<List<T>>
    fun getByCategory(category: String): Flow<List<T>>
}