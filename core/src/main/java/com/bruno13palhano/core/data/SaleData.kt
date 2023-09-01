package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface SaleData<T> : DataOperations<T> {
    fun getByCustomerId(customerId: Long): Flow<List<T>>
    fun getLastSales(offset: Int, limit: Int): Flow<List<T>>
    fun getAllStockSales(offset: Int, limit: Int): Flow<List<T>>
    fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<T>>
}