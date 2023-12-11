package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.RepositoryOperations
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface SaleRepository : RepositoryOperations<Sale>, Syncable {
    suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    )
    fun getByCustomerId(customerId: Long): Flow<List<Sale>>
    fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>>
    fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>>
    fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>>
    fun getAllCanceledSales(): Flow<List<Sale>>
    fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<Sale>>
    fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>>
    fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<Sale>>
    suspend fun cancelSale(saleId: Long)
    fun getDebitSales(): Flow<List<Sale>>
    fun getSalesByCustomerName(isPaidByCustomer: Boolean, isOrderedAsc: Boolean): Flow<List<Sale>>
    fun getSalesBySalePrice(isPaidByCustomer: Boolean, isOrderedAsc: Boolean): Flow<List<Sale>>
    fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>>
    fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>>
}