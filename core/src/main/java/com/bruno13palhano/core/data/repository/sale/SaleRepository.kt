package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SaleRepository @Inject constructor(
    @InternalSaleLight private val saleData: SaleData<Sale>
) : SaleData<Sale> {
    override suspend fun insert(model: Sale): Long {
        return saleData.insert(model = model)
    }

    override suspend fun update(model: Sale) {
        saleData.update(model = model)
    }

    override suspend fun cancelSale(saleId: Long) {
        saleData.cancelSale(saleId = saleId)
    }

    override suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        saleData.insertItems(
            sale = sale,
            stockOrder = stockOrder,
            delivery = delivery,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleData.getByCustomerId(customerId = customerId)
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getLastSales(offset = offset, limit = limit)
    }

    override fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getAllStockSales(offset = offset, limit = limit)
    }

    override fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getAllOrdersSales(offset = offset, limit = limit)
    }

    override suspend fun deleteById(id: Long) {
        saleData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Sale>> {
        return saleData.getAll()
    }

    override fun getAllCanceledSales(): Flow<List<Sale>> {
        return saleData.getAllCanceledSales()
    }

    override fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getCanceledByName(isOrderedAsc = isOrderedAsc)
    }

    override fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getCanceledByCustomerName(isOrderedAsc = isOrderedAsc)
    }

    override fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getCanceledByPrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getSalesByCustomerName(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return saleData.getSalesByCustomerName(
            isPaidByCustomer = isPaidByCustomer,
            isOrderedAsc = isOrderedAsc
        )
    }

    override fun getSalesBySalePrice(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return saleData.getSalesBySalePrice(
            isPaidByCustomer = isPaidByCustomer,
            isOrderedAsc = isOrderedAsc
        )
    }

    override fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getAllSalesByCustomerName(isOrderedAsc = isOrderedAsc)
    }

    override fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getAllSalesBySalePrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleData.getById(id = id)
    }

    override fun getLast(): Flow<Sale> {
        return saleData.getLast()
    }

    override fun getDebitSales(): Flow<List<Sale>> {
        return saleData.getDebitSales()
    }
}