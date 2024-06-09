package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Sale
import kotlinx.coroutines.flow.Flow

interface SaleData : DataOperations<Sale> {
    suspend fun insert(
        model: Sale,
        version: DataVersion,
        pushed: Boolean,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long, itemQuantity: Int) -> Unit
    ): Long

    suspend fun update(
        model: Sale,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (stockQuantity: Int) -> Unit
    )

    fun getByCustomerId(customerId: Long): Flow<List<Sale>>

    fun getOrdersByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>>

    fun getOrdersBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>>

    fun getDeliveries(delivered: Boolean): Flow<List<Sale>>

    fun getLastSales(
        offset: Int,
        limit: Int
    ): Flow<List<Sale>>

    fun getAmazonSales(): Flow<List<Sale>>

    fun getAllStockSales(
        offset: Int,
        limit: Int
    ): Flow<List<Sale>>

    fun getAllOrdersSales(
        offset: Int,
        limit: Int
    ): Flow<List<Sale>>

    fun getAllCanceledSales(): Flow<List<Sale>>

    fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<Sale>>

    fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>>

    fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<Sale>>

    fun searchAmazonSales(search: String): Flow<List<Sale>>

    suspend fun cancelSale(saleId: Long)

    suspend fun getAllSales(): List<Sale>

    suspend fun getAllAmazonSales(): List<Sale>

    fun getDebitSales(): Flow<List<Sale>>

    fun getSalesByCustomerName(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>>

    fun getSalesBySalePrice(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>>

    fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>>

    fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>>
}