package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class TestSaleRepository : SaleData<Sale> {
    private val sales = mutableListOf<Sale>()

    override suspend fun insert(model: Sale): Long {
        sales.add(model)
        return model.id
    }

    override suspend fun update(model: Sale) {
        val index = getIndex(id = model.id, list = sales)
        if (isIndexValid(index = index)) sales[index] = model
    }

    override suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        sales.add(sale)
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return flowOf(sales.filter { it.customerId == customerId })
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return flowOf(sales)
    }

    override fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return flowOf(sales.filter { !it.isOrderedByCustomer })
    }

    override fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return flowOf(sales.filter { it.isOrderedByCustomer })
    }

    override fun getAllCanceledSales(): Flow<List<Sale>> {
        return flowOf(sales.filter { it.canceled })
    }

    override fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { it.canceled }.sortedBy { it.name })
        } else {
            flowOf(sales.filter { it.canceled }.sortedByDescending { it.name })
        }
    }

    override fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { it.canceled }.sortedBy { it.customerName })
        } else {
            flowOf(sales.filter { it.canceled }.sortedByDescending { it.customerName })
        }
    }

    override fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { it.canceled }.sortedBy { it.salePrice })
        } else {
            flowOf(sales.filter { it.canceled }.sortedByDescending { it.salePrice })
        }
    }

    override suspend fun cancelSale(saleId: Long) {
        val index = getIndex(id = saleId, list = sales)

        if (isIndexValid(index = index)) {
            val currentSale = sales[index]

            val sale = Sale(
                id = currentSale.id,
                productId = currentSale.productId,
                stockOrderId = currentSale.stockOrderId,
                customerId = currentSale.customerId,
                name = currentSale.name,
                customerName = currentSale.customerName,
                photo = currentSale.photo,
                quantity = currentSale.quantity,
                purchasePrice = currentSale.purchasePrice,
                salePrice = currentSale.salePrice,
                deliveryPrice = currentSale.deliveryPrice,
                categories = currentSale.categories,
                company = currentSale.company,
                dateOfSale = currentSale.dateOfSale,
                dateOfPayment = currentSale.dateOfPayment,
                isOrderedByCustomer = currentSale.isOrderedByCustomer,
                isPaidByCustomer = currentSale.isPaidByCustomer,
                canceled = true
            )

            sales[index] = sale
        }
    }

    override fun getDebitSales(): Flow<List<Sale>> =
        flowOf(sales.filter { !it.isPaidByCustomer && !it.canceled})

    override fun getSalesByCustomerName(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            if (isPaidByCustomer) {
                flowOf(sales.filter { it.isPaidByCustomer && !it.canceled }
                    .sortedBy { it.customerName })
            } else {
                flowOf(sales.filter { !it.isPaidByCustomer && !it.canceled }
                    .sortedBy { it.customerName })
            }
        } else {
            if (isPaidByCustomer) {
                flowOf(sales.filter { it.isPaidByCustomer && !it.canceled }
                    .sortedByDescending { it.customerName })
            } else {
                flowOf(sales.filter { !it.isPaidByCustomer && !it.canceled }
                    .sortedByDescending { it.customerName })
            }
        }
    }

    override fun getSalesBySalePrice(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            if (isPaidByCustomer) {
                flowOf(sales.filter { it.isPaidByCustomer && !it.canceled }
                    .sortedBy { it.salePrice })
            } else {
                flowOf(sales.filter { !it.isPaidByCustomer && !it.canceled }
                    .sortedBy { it.salePrice })
            }
        } else {
            if (isPaidByCustomer) {
                flowOf(sales.filter { it.isPaidByCustomer && !it.canceled}
                    .sortedByDescending { it.salePrice })
            } else {
                flowOf(sales.filter { !it.isPaidByCustomer && !it.canceled}
                    .sortedByDescending { it.salePrice })
            }
        }
    }

    override fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { !it.canceled }.sortedBy { it.customerName })
        } else {
            flowOf(sales.filter { !it.canceled }.sortedByDescending { it.customerName })
        }
    }

    override fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { !it.canceled }.sortedBy { it.salePrice })
        } else {
            flowOf(sales.filter { !it.canceled }.sortedByDescending { it.salePrice })
        }
    }

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = sales)
        if(isIndexValid(index = index)) sales.removeAt(index)
    }

    override fun getAll(): Flow<List<Sale>> = flow {
        emit(sales.filter { !it.canceled })
    }

    override fun getById(id: Long): Flow<Sale> {
        val index = getIndex(id = id, list = sales)
        return if (isIndexValid(index = index)) flowOf(sales[index]) else flowOf()
    }

    override fun getLast(): Flow<Sale> = flowOf(sales.last())
}