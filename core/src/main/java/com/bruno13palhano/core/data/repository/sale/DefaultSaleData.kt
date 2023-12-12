package com.bruno13palhano.core.data.repository.sale

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.DeliveryTableQueries
import cache.SaleTableQueries
import cache.StockTableQueries
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultSaleData @Inject constructor(
    private val saleQueries: SaleTableQueries,
    private val deliveryQueries: DeliveryTableQueries,
    private val stockOrderQueries: StockTableQueries,
    private val versionQueries: VersionTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleData {
    override suspend fun insert(
        model: Sale,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long
    ) -> Unit): Long {
        var id = 0L

        try {
            if (model.isNew()) {
                saleQueries.transaction {
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
                        timestamp = model.timestamp
                    )

                    versionQueries.insert(name = version.name, timestamp = version.timestamp)

                    id = saleQueries.getLastId().executeAsOne()
                    onSuccess(id)
                }
            } else {
                saleQueries.transaction {
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
                        timestamp = model.timestamp
                    )

                    versionQueries.insertWithId(
                        id = version.id,
                        name = version.name,
                        timestamp = version.timestamp
                    )

                    id = model.id
                    onSuccess(model.id)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(1)
        }

        return id
    }

    override suspend fun update(
        model: Sale,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            saleQueries.transaction {
                saleQueries.update(
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
                    timestamp = model.timestamp,
                    id = model.id
                )

                versionQueries.update(
                    name = version.name,
                    timestamp = version.timestamp,
                    id = version.id
                )

                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(2)
        }
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
        stockItem: StockItem,
        delivery: Delivery,
        saleVersion: DataVersion,
        deliveryVersion: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (saleId: Long, stockId: Long, deliveryId: Long) -> Unit
    ) {
        try {
            if (sale.isNew()) {
                if (sale.isOrderedByCustomer) {
                    var saleId = 0L

                    saleQueries.transaction {
                        saleQueries.transaction {
                            saleQueries.insert(
                                productId = sale.productId,
                                customerId = sale.customerId,
                                stockOrderId = 0L,
                                quantity = sale.quantity.toLong(),
                                salePrice = sale.salePrice.toDouble(),
                                purchasePrice = sale.purchasePrice.toDouble(),
                                dateOfSale = sale.dateOfSale,
                                dateOfPayment = sale.dateOfPayment,
                                isOrderedByCustomer = true,
                                isPaidByCustomer = sale.isPaidByCustomer,
                                canceled = sale.canceled,
                                timestamp = sale.timestamp
                            )
                            saleId = saleQueries.getLastId().executeAsOne()
                            deliveryQueries.insert(
                                saleId = saleId,
                                deliveryPrice = delivery.deliveryPrice.toDouble(),
                                shippingDate = delivery.shippingDate,
                                deliveryDate = delivery.deliveryDate,
                                delivered = delivery.delivered,
                                timestamp = delivery.timestamp
                            )
                        }

                        versionQueries.insertWithId(
                            id = saleVersion.id,
                            name = saleVersion.name,
                            timestamp = saleVersion.timestamp
                        )

                        versionQueries.insertWithId(
                            id = deliveryVersion.id,
                            name = deliveryVersion.name,
                            timestamp = deliveryVersion.timestamp
                        )

                        onSuccess(saleId, 0L, deliveryQueries.getLastId().executeAsOne())
                    }
                } else {
                    val quantity = stockItem.quantity - sale.quantity
                    var saleId = 0L

                    if (quantity >= 0) {
                        saleQueries.transaction {
                            stockOrderQueries.updateStockOrderQuantity(
                                id = stockItem.id,
                                quantity = quantity.toLong()
                            )
                            saleQueries.insert(
                                productId = sale.productId,
                                customerId = sale.customerId,
                                stockOrderId = stockItem.id,
                                quantity = sale.quantity.toLong(),
                                purchasePrice = sale.purchasePrice.toDouble(),
                                salePrice = sale.salePrice.toDouble(),
                                dateOfSale = sale.dateOfSale,
                                dateOfPayment = sale.dateOfPayment,
                                isOrderedByCustomer = false,
                                isPaidByCustomer = sale.isPaidByCustomer,
                                canceled = sale.canceled,
                                timestamp = sale.timestamp
                            )
                            saleId = saleQueries.getLastId().executeAsOne()
                            deliveryQueries.insert(
                                saleId = saleId,
                                deliveryPrice = delivery.deliveryPrice.toDouble(),
                                shippingDate = delivery.shippingDate,
                                deliveryDate = delivery.deliveryDate,
                                delivered = delivery.delivered,
                                timestamp = delivery.timestamp
                            )

                            versionQueries.insertWithId(
                                id = saleVersion.id,
                                name = saleVersion.name,
                                timestamp = saleVersion.timestamp
                            )

                            versionQueries.insertWithId(
                                id = deliveryVersion.id,
                                name = deliveryVersion.name,
                                timestamp = deliveryVersion.timestamp
                            )

                            onSuccess(saleId, stockItem.id, deliveryQueries.getLastId().executeAsOne())
                        }
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
                            timestamp = sale.timestamp
                        )
                        deliveryQueries.insertWithId(
                            id = delivery.id,
                            saleId = delivery.saleId,
                            deliveryPrice = delivery.deliveryPrice.toDouble(),
                            shippingDate = delivery.shippingDate,
                            deliveryDate = delivery.deliveryDate,
                            delivered = delivery.delivered,
                            timestamp = delivery.timestamp
                        )

                        versionQueries.insertWithId(
                            id = saleVersion.id,
                            name = saleVersion.name,
                            timestamp = saleVersion.timestamp
                        )

                        versionQueries.insertWithId(
                            id = deliveryVersion.id,
                            name = deliveryVersion.name,
                            timestamp = deliveryVersion.timestamp
                        )

                        onSuccess(sale.id, 0L, delivery.id)
                    }
                } else {
                    val quantity = stockItem.quantity - sale.quantity

                    if (quantity >= 0) {
                        saleQueries.transaction {
                            stockOrderQueries.updateStockOrderQuantity(
                                id = stockItem.id,
                                quantity = quantity.toLong()
                            )
                            saleQueries.insertWithId(
                                id = sale.id,
                                productId = sale.productId,
                                customerId = sale.customerId,
                                stockOrderId = stockItem.id,
                                quantity = sale.quantity.toLong(),
                                purchasePrice = sale.purchasePrice.toDouble(),
                                salePrice = sale.salePrice.toDouble(),
                                dateOfSale = sale.dateOfSale,
                                dateOfPayment = sale.dateOfPayment,
                                isOrderedByCustomer = false,
                                isPaidByCustomer = sale.isPaidByCustomer,
                                canceled = sale.canceled,
                                timestamp = sale.timestamp
                            )
                            deliveryQueries.insertWithId(
                                id = delivery.id,
                                saleId = delivery.saleId,
                                deliveryPrice = delivery.deliveryPrice.toDouble(),
                                shippingDate = delivery.shippingDate,
                                deliveryDate = delivery.deliveryDate,
                                delivered = delivery.delivered,
                                timestamp = delivery.timestamp
                            )

                            versionQueries.insertWithId(
                                id = saleVersion.id,
                                name = saleVersion.name,
                                timestamp = saleVersion.timestamp
                            )

                            versionQueries.insertWithId(
                                id = deliveryVersion.id,
                                name = deliveryVersion.name,
                                timestamp = deliveryVersion.timestamp
                            )

                            onSuccess(sale.id, stockItem.id, delivery.id)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(1)
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

    override suspend fun deleteById(
        id: Long,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            saleQueries.transaction {
                saleQueries.delete(id = id)

                versionQueries.update(
                    name = version.name,
                    timestamp = version.timestamp,
                    id = version.id
                )

                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(3)
        }
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
            timestamp = timestamp
        )
    }
}