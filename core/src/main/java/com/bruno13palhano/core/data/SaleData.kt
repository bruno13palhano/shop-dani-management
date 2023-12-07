package com.bruno13palhano.core.data

import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface SaleData<T> : DataOperations<T>, Syncable {
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
    fun getAllCanceledSales(): Flow<List<T>>
    fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<T>>
    suspend fun cancelSale(saleId: Long)
    fun getDebitSales(): Flow<List<T>>
    fun getSalesByCustomerName(isPaidByCustomer: Boolean, isOrderedAsc: Boolean): Flow<List<T>>
    fun getSalesBySalePrice(isPaidByCustomer: Boolean, isOrderedAsc: Boolean): Flow<List<T>>
    fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<T>>
}