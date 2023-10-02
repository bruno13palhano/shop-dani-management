package com.bruno13palhano.core.data

import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow

interface SaleData<T> : DataOperations<T> {
    suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )
    fun getByCustomerId(customerId: Long): Flow<List<T>>
    fun getLastSales(offset: Int, limit: Int): Flow<List<T>>
    fun getAllStockSales(offset: Int, limit: Int): Flow<List<T>>
    fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<T>>
    fun getCanceledSales(offset: Int, limit: Int): Flow<List<T>>
    suspend fun cancelSale(saleId: Long)
    fun getDebitSales(): Flow<List<T>>
    fun getSalesByCustomerName(isPaidByCustomer: Boolean, isOrderedAsc: Boolean): Flow<List<T>>
    fun getSalesBySalePrice(isPaidByCustomer: Boolean, isOrderedAsc: Boolean): Flow<List<T>>
}