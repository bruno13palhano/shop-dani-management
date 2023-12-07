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
import com.bruno13palhano.core.model.isNew
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class SaleLight @Inject constructor(
    private val saleQueries: SaleTableQueries,
    private val deliveryQueries: DeliveryTableQueries,
    private val stockOrderQueries: StockOrderTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleData<Sale> {
    override suspend fun insert(model: Sale): Long {
        if (model.isNew()) {
            saleQueries.insert(
                productId = model.productId,
                customerId = model.customerId,
                stockOrderId = model.stockOrderId,
                quantity = model.quantity.toLong(),
                purchasePrice = model.purchasePrice.toDouble(),
                salePrice = model.salePrice.toDouble(),
                dateOfSale = model.dateOfSale,
                dateOfPayment = model.dateOfPayment,
                isOrderedByCustomer = model.isOrderedByCustomer,
                isPaidByCustomer = model.isPaidByCustomer,
                canceled = model.canceled,
                timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .format(model.timestamp)
            )

            return saleQueries.getLastId().executeAsOne()
        } else {
            saleQueries.insertWithId(
                id = model.id,
                productId = model.productId,
                customerId = model.customerId,
                stockOrderId = model.stockOrderId,
                quantity = model.quantity.toLong(),
                purchasePrice = model.purchasePrice.toDouble(),
                salePrice = model.salePrice.toDouble(),
                dateOfSale = model.dateOfSale,
                dateOfPayment = model.dateOfPayment,
                isOrderedByCustomer = model.isOrderedByCustomer,
                isPaidByCustomer = model.isPaidByCustomer,
                canceled = model.canceled,
                timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .format(model.timestamp)
            )

            return model.id
        }
    }

    override suspend fun update(model: Sale) {
        saleQueries.update(
            productId = model.productId,
            customerId =  model.customerId,
            stockOrderId = model.stockOrderId,
            quantity = model.quantity.toLong(),
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            dateOfSale = model.dateOfSale,
            dateOfPayment = model.dateOfPayment,
            isOrderedByCustomer = model.isOrderedByCustomer,
            isPaidByCustomer = model.isPaidByCustomer,
            canceled = model.canceled,
            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(model.timestamp),
            id = model.id
        )
    }

    override suspend fun cancelSale(saleId: Long) {
        val sale = saleQueries.getById(id = saleId).executeAsOne()
        saleQueries.setCanceledSale(id = saleId)
        deliveryQueries.deleteBySaleId(saleId = saleId)
        stockOrderQueries.updateStockOrderQuantity(
            id = sale.stockOrderId,
            quantity = stockOrderQueries.getStockQuantity(id = sale.stockOrderId).executeAsOne()
                    + sale.quantity
        )
    }

    override suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (sale.isNew()) {
            if (sale.isOrderedByCustomer) {
                saleQueries.transaction {
                    saleQueries.insert(
                        productId = sale.productId,
                        customerId = sale.customerId,
                        stockOrderId = sale.stockOrderId,
                        quantity = sale.quantity.toLong(),
                        salePrice = sale.salePrice.toDouble(),
                        purchasePrice = sale.purchasePrice.toDouble(),
                        dateOfSale = sale.dateOfSale,
                        dateOfPayment = sale.dateOfPayment,
                        isOrderedByCustomer = true,
                        isPaidByCustomer = sale.isPaidByCustomer,
                        canceled = sale.canceled,
                        timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .format(sale.timestamp)
                    )
                    deliveryQueries.insert(
                        saleId = saleQueries.getLastId().executeAsOne(),
                        deliveryPrice = delivery.deliveryPrice.toDouble(),
                        shippingDate = delivery.shippingDate,
                        deliveryDate = delivery.deliveryDate,
                        delivered = delivery.delivered,
                        timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .format(delivery.timestamp)
                    )
                    stockOrderQueries.insert(
                        productId = stockOrder.productId,
                        date = stockOrder.date,
                        validity = stockOrder.validity,
                        quantity = sale.quantity.toLong(),
                        purchasePrice = stockOrder.purchasePrice.toDouble(),
                        salePrice = stockOrder.salePrice.toDouble(),
                        isOrderedByCustomer = true,
                        isPaid = stockOrder.isPaid,
                        timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .format(stockOrder.timestamp)
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
                            stockOrderId = stockOrder.id,
                            quantity = sale.quantity.toLong(),
                            purchasePrice = sale.purchasePrice.toDouble(),
                            salePrice = sale.salePrice.toDouble(),
                            dateOfSale = sale.dateOfSale,
                            dateOfPayment = sale.dateOfPayment,
                            isOrderedByCustomer = false,
                            isPaidByCustomer = sale.isPaidByCustomer,
                            canceled = sale.canceled,
                            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                .format(sale.timestamp)
                        )
                        deliveryQueries.insert(
                            saleId = saleQueries.getLastId().executeAsOne(),
                            deliveryPrice = delivery.deliveryPrice.toDouble(),
                            shippingDate = delivery.shippingDate,
                            deliveryDate = delivery.deliveryDate,
                            delivered = delivery.delivered,
                            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                .format(delivery.timestamp)
                        )
                    }
                    onSuccess()
                } else {
                    onError()
                }
            }
        } else {
            if (sale.isOrderedByCustomer) {
                saleQueries.transaction {
                    saleQueries.insertWithId(
                        id = sale.id,
                        productId = sale.productId,
                        customerId = sale.customerId,
                        stockOrderId = sale.stockOrderId,
                        quantity = sale.quantity.toLong(),
                        salePrice = sale.salePrice.toDouble(),
                        purchasePrice = sale.purchasePrice.toDouble(),
                        dateOfSale = sale.dateOfSale,
                        dateOfPayment = sale.dateOfPayment,
                        isOrderedByCustomer = true,
                        isPaidByCustomer = sale.isPaidByCustomer,
                        canceled = sale.canceled,
                        timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .format(sale.timestamp)
                    )
                    deliveryQueries.insertWithId(
                        id = delivery.id,
                        saleId = delivery.saleId,
                        deliveryPrice = delivery.deliveryPrice.toDouble(),
                        shippingDate = delivery.shippingDate,
                        deliveryDate = delivery.deliveryDate,
                        delivered = delivery.delivered,
                        timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .format(delivery.timestamp)
                    )
                    stockOrderQueries.insertWithId(
                        id = stockOrder.id,
                        productId = stockOrder.productId,
                        date = stockOrder.date,
                        validity = stockOrder.validity,
                        quantity = sale.quantity.toLong(),
                        purchasePrice = stockOrder.purchasePrice.toDouble(),
                        salePrice = stockOrder.salePrice.toDouble(),
                        isOrderedByCustomer = true,
                        isPaid = stockOrder.isPaid,
                        timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .format(stockOrder.timestamp)
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
                        saleQueries.insertWithId(
                            id = sale.id,
                            productId = sale.productId,
                            customerId = sale.customerId,
                            stockOrderId = stockOrder.id,
                            quantity = sale.quantity.toLong(),
                            purchasePrice = sale.purchasePrice.toDouble(),
                            salePrice = sale.salePrice.toDouble(),
                            dateOfSale = sale.dateOfSale,
                            dateOfPayment = sale.dateOfPayment,
                            isOrderedByCustomer = false,
                            isPaidByCustomer = sale.isPaidByCustomer,
                            canceled = sale.canceled,
                            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                .format(sale.timestamp)
                        )
                        deliveryQueries.insertWithId(
                            id = delivery.id,
                            saleId = delivery.saleId,
                            deliveryPrice = delivery.deliveryPrice.toDouble(),
                            shippingDate = delivery.shippingDate,
                            deliveryDate = delivery.deliveryDate,
                            delivered = delivery.delivered,
                            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                .format(delivery.timestamp)
                        )
                    }
                    onSuccess()
                } else {
                    onError()
                }
            }
        }
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleQueries.getByCustomerId(customerId = customerId, mapper = ::mapSale)
            .asFlow().mapToList(ioDispatcher)
            .catch { it.printStackTrace() }
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
        saleQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<Sale>> {
        return saleQueries.getAll(mapper = ::mapSale)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getAllCanceledSales(): Flow<List<Sale>> {
        return saleQueries.getAllCanceledSales(mapper = ::mapSale).asFlow().mapToList(ioDispatcher)
    }

    override fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getCanceledByNameAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getCanceledByNameDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getCanceledByCustomerNameAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getCanceledByCustomerNameDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getCanceledByPriceAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getCanceledByPriceDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getSalesByCustomerName(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getSalesByCustomerNameAsc(
                isPaidByCustomer = isPaidByCustomer, mapper = ::mapSale
            ).asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getSalesByCustomerNameDesc(
                isPaidByCustomer = isPaidByCustomer,
                mapper = ::mapSale
            ).asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getSalesBySalePrice(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getSalesBySalePriceAsc(
                isPaidByCustomer = isPaidByCustomer,
                mapper = ::mapSale
            ).asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getSalesBySalePriceDesc(
                isPaidByCustomer = isPaidByCustomer,
                mapper = ::mapSale
            ).asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getAllSalesByCustomerNameAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getAllSalesByCustomerNameDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getAllSalesBySalePriceAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            saleQueries.getAllSalesBySalePriceDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleQueries.getById(id = id, mapper = ::mapSale)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Sale> {
        return saleQueries.getLast(mapper = ::mapSale)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDebitSales(): Flow<List<Sale>> {
        return saleQueries.getDebitSales(mapper = ::mapSale)
            .asFlow().mapToList(ioDispatcher)
    }

    private fun mapSale(
        id: Long,
        productId: Long,
        customerId: Long,
        stockOrderId: Long,
        name: String,
        customerName: String,
        photo: ByteArray,
        quantity: Long,
        purchasePrice: Double,
        salePrice: Double,
        deliveryPrice: Double,
        categories: List<Category>,
        company: String,
        dateOfSale: Long,
        dateOfPayment: Long,
        isOrderedByCustomer: Boolean,
        isPaidByCustomer: Boolean,
        canceled: Boolean,
        timestamp: String
    ): Sale {
        return Sale(
            id = id,
            productId = productId,
            customerId = customerId,
            stockOrderId = stockOrderId,
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
            isPaidByCustomer = isPaidByCustomer,
            canceled = canceled,
            timestamp = OffsetDateTime.parse(timestamp)
        )
    }
}