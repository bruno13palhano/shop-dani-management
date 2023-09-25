package com.bruno13palhano.core.data.repository.sale

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.DeliveryTableQueries
import cache.SaleTableQueries
import cache.StockOrderTableQueries
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SaleLight @Inject constructor(
    private val saleQueries: SaleTableQueries,
    private val deliveryQueries: DeliveryTableQueries,
    private val stockOrderQueries: StockOrderTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleData<Sale> {
    override suspend fun insert(model: Sale): Long {
        saleQueries.insert(
            productId = model.productId,
            customerId =  model.customerId,
            quantity = model.quantity.toLong(),
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            dateOfSale = model.dateOfSale,
            dateOfPayment = model.dateOfPayment,
            isOrderedByCustomer = model.isOrderedByCustomer,
            isPaidByCustomer = model.isPaidByCustomer
        )
        return saleQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Sale) {
        saleQueries.update(
            productId = model.productId,
            customerId =  model.customerId,
            quantity = model.quantity.toLong(),
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            dateOfSale = model.dateOfSale,
            dateOfPayment = model.dateOfPayment,
            isOrderedByCustomer = model.isOrderedByCustomer,
            isPaidByCustomer = model.isPaidByCustomer,
            id = model.id
        )
    }

    override suspend fun delete(model: Sale) {
        saleQueries.delete(id = model.id)
    }

    override suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (sale.isOrderedByCustomer) {
            saleQueries.transaction {
                saleQueries.insert(
                    productId = sale.productId,
                    customerId = sale.customerId,
                    quantity = sale.quantity.toLong(),
                    salePrice = sale.salePrice.toDouble(),
                    purchasePrice = sale.purchasePrice.toDouble(),
                    dateOfSale = sale.dateOfSale,
                    dateOfPayment = sale.dateOfPayment,
                    isOrderedByCustomer = true,
                    isPaidByCustomer = sale.isPaidByCustomer
                )
                deliveryQueries.insert(
                    saleId = saleQueries.getLastId().executeAsOne(),
                    deliveryPrice = delivery.deliveryPrice.toDouble(),
                    shippingDate = delivery.shippingDate,
                    deliveryDate = delivery.deliveryDate,
                    delivered = delivery.delivered
                )
                stockOrderQueries.insert(
                    productId = saleQueries.getLastId().executeAsOne(),
                    date = stockOrder.date,
                    validity = stockOrder.validity,
                    quantity = stockOrder.quantity.toLong(),
                    purchasePrice = stockOrder.purchasePrice.toDouble(),
                    salePrice = stockOrder.salePrice.toDouble(),
                    isOrderedByCustomer = true
                )
            }
            onSuccess()
        } else {
            val quantity = stockOrder.quantity - sale.quantity

            if (quantity >= 0) {
                saleQueries.transaction {
                    stockOrderQueries.updateStockOrderQuantity(
                        id = stockOrder.id,
                        quantity = quantity.toLong()
                    )
                    saleQueries.insert(
                        productId = sale.productId,
                        customerId = sale.customerId,
                        quantity = sale.quantity.toLong(),
                        purchasePrice = sale.purchasePrice.toDouble(),
                        salePrice = sale.salePrice.toDouble(),
                        dateOfSale = sale.dateOfSale,
                        dateOfPayment = sale.dateOfPayment,
                        isOrderedByCustomer = false,
                        isPaidByCustomer = sale.isPaidByCustomer
                    )
                    deliveryQueries.insert(
                        saleId = saleQueries.getLastId().executeAsOne(),
                        deliveryPrice = delivery.deliveryPrice.toDouble(),
                        shippingDate = delivery.shippingDate,
                        deliveryDate = delivery.deliveryDate,
                        delivered = delivery.delivered
                    )
                }
                onSuccess()
            } else {
                onError()
            }
        }
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleQueries.getByCustomerId(customerId = customerId, mapper = ::mapSale)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleQueries.getLastSales(
            offset = offset.toLong(),
            limit = limit.toLong(),
            mapper = ::mapSale
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleQueries.getAllStockSales(
            offset = offset.toLong(),
            limit = limit.toLong(),
            mapper = ::mapSale
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleQueries.getAllOrdersSales(
            offset = offset.toLong(),
            limit = limit.toLong(),
            mapper = ::mapSale
        ).asFlow().mapToList(ioDispatcher)
    }

    override suspend fun deleteById(id: Long) {
        return saleQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<Sale>> {
        return saleQueries.getAll(mapper = ::mapSale)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleQueries.getById(id = id, mapper = ::mapSale)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun getLast(): Flow<Sale> {
        return saleQueries.getLast(mapper = ::mapSale)
            .asFlow().mapToOne(ioDispatcher)
    }

    private fun mapSale(
        id: Long,
        productId: Long,
        customerId: Long,
        name: String,
        customerName: String,
        photo: String,
        quantity: Long,
        purchasePrice: Double,
        salePrice: Double,
        deliveryPrice: Double,
        categories: List<Category>,
        company: String,
        dateOfSale: Long,
        dateOfPayment: Long,
        isOrderedByCustomer: Boolean,
        isPaidByCustomer: Boolean
    ): Sale {
        return Sale(
            id = id,
            productId = productId,
            customerId = customerId,
            name = name,
            customerName = customerName,
            photo = photo,
            quantity = quantity.toInt(),
            purchasePrice = purchasePrice.toFloat(),
            salePrice = salePrice.toFloat(),
            deliveryPrice = deliveryPrice.toFloat(),
            categories = categories,
            company = company,
            dateOfSale = dateOfSale,
            dateOfPayment = dateOfPayment,
            isOrderedByCustomer = isOrderedByCustomer,
            isPaidByCustomer = isPaidByCustomer
        )
    }
}