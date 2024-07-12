package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeSaleRepository : SaleRepository {
    private val sales = mutableListOf<Sale>()

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return flowOf(sales.filter { it.customerId == customerId })
    }

    override fun getOrdersByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { it.isOrderedByCustomer }.sortedBy { it.customerName })
        } else {
            flowOf(sales.filter { it.isOrderedByCustomer }.sortedByDescending { it.customerName })
        }
    }

    override fun getOrdersBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            flowOf(sales.filter { it.isOrderedByCustomer }.sortedBy { it.salePrice })
        } else {
            flowOf(sales.filter { it.isOrderedByCustomer }.sortedByDescending { it.salePrice })
        }
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Sale>> {
        return flowOf(sales.filter { it.delivered == delivered })
    }

    override fun getLastSales(
        offset: Int,
        limit: Int
    ): Flow<List<Sale>> {
        return flowOf(sales)
    }

    override fun getAmazonSale(): Flow<List<Sale>> {
        TODO("Not yet implemented")
    }

    override fun getAllStockSales(
        offset: Int,
        limit: Int
    ): Flow<List<Sale>> {
        return flowOf(sales.filter { !it.isOrderedByCustomer })
    }

    override fun getAllOrdersSales(
        offset: Int,
        limit: Int
    ): Flow<List<Sale>> {
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

    override fun searchAmazonSales(search: String): Flow<List<Sale>> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelSale(saleId: Long) {
        val index = getIndex(id = saleId, list = sales)

        if (isIndexValid(index = index)) {
            val currentSale = sales[index]

            val sale =
                Sale(
                    id = currentSale.id,
                    productId = currentSale.productId,
                    stockId = currentSale.stockId,
                    customerId = currentSale.customerId,
                    name = currentSale.name,
                    customerName = currentSale.customerName,
                    photo = currentSale.photo,
                    address = currentSale.address,
                    phoneNumber = currentSale.phoneNumber,
                    quantity = currentSale.quantity,
                    purchasePrice = currentSale.purchasePrice,
                    salePrice = currentSale.salePrice,
                    deliveryPrice = currentSale.deliveryPrice,
                    categories = currentSale.categories,
                    company = currentSale.company,
                    amazonCode = currentSale.amazonCode,
                    amazonRequestNumber = currentSale.amazonRequestNumber,
                    amazonTax = currentSale.amazonTax,
                    amazonProfit = currentSale.amazonProfit,
                    amazonSKU = currentSale.amazonSKU,
                    resaleProfit = currentSale.resaleProfit,
                    totalProfit = currentSale.totalProfit,
                    dateOfSale = currentSale.dateOfSale,
                    dateOfPayment = currentSale.dateOfPayment,
                    shippingDate = currentSale.shippingDate,
                    deliveryDate = currentSale.deliveryDate,
                    isOrderedByCustomer = currentSale.isOrderedByCustomer,
                    isPaidByCustomer = currentSale.isPaidByCustomer,
                    delivered = currentSale.delivered,
                    canceled = true,
                    isAmazon = currentSale.isAmazon,
                    timestamp = currentSale.timestamp
                )

            sales[index] = sale
        }
    }

    override suspend fun exportExcelSheet(sheetName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun exportAmazonExcelSheet(sheetName: String) {
        TODO("Not yet implemented")
    }

    override fun getDebitSales(): Flow<List<Sale>> = flowOf(sales.filter { !it.isPaidByCustomer && !it.canceled })

    override fun getSalesByCustomerName(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            if (isPaidByCustomer) {
                flowOf(
                    sales.filter { it.isPaidByCustomer && !it.canceled }
                        .sortedBy { it.customerName }
                )
            } else {
                flowOf(
                    sales.filter { !it.isPaidByCustomer && !it.canceled }
                        .sortedBy { it.customerName }
                )
            }
        } else {
            if (isPaidByCustomer) {
                flowOf(
                    sales.filter { it.isPaidByCustomer && !it.canceled }
                        .sortedByDescending { it.customerName }
                )
            } else {
                flowOf(
                    sales.filter { !it.isPaidByCustomer && !it.canceled }
                        .sortedByDescending { it.customerName }
                )
            }
        }
    }

    override fun getSalesBySalePrice(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            if (isPaidByCustomer) {
                flowOf(
                    sales.filter { it.isPaidByCustomer && !it.canceled }
                        .sortedBy { it.salePrice }
                )
            } else {
                flowOf(
                    sales.filter { !it.isPaidByCustomer && !it.canceled }
                        .sortedBy { it.salePrice }
                )
            }
        } else {
            if (isPaidByCustomer) {
                flowOf(
                    sales.filter { it.isPaidByCustomer && !it.canceled }
                        .sortedByDescending { it.salePrice }
                )
            } else {
                flowOf(
                    sales.filter { !it.isPaidByCustomer && !it.canceled }
                        .sortedByDescending { it.salePrice }
                )
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

    override suspend fun insert(
        model: Sale,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        sales.add(model)
        onSuccess(model.id)
        return model.id
    }

    override suspend fun update(
        model: Sale,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = model.id, list = sales)
        if (isIndexValid(index = index)) {
            sales[index] = model
            onSuccess()
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = id, list = sales)
        if (isIndexValid(index = index)) {
            sales.removeAt(index)
            onSuccess()
        }
    }

    override fun getAll(): Flow<List<Sale>> =
        flow {
            emit(sales.filter { !it.canceled })
        }

    override fun getById(id: Long): Flow<Sale> {
        val index = getIndex(id = id, list = sales)
        return if (isIndexValid(index = index)) flowOf(sales[index]) else flowOf()
    }

    override fun getLast(): Flow<Sale> = flowOf(sales.last())

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}