package com.bruno13palhano.core.data.repository.sale

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.SaleTableQueries
import cache.StockTableQueries
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultSaleData @Inject constructor(
    private val saleQueries: SaleTableQueries,
    private val stockQueries: StockTableQueries,
    private val versionQueries: VersionTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleData {
    override suspend fun insert(
        model: Sale,
        version: DataVersion,
        pushed: Boolean,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long, stockItem: Int) -> Unit
    ): Long {
        var id = 0L
        var quantity: Int
        var newQuantity = 0
        val stockVersion = Versions.stockVersion(model.timestamp)

        try {
            if (model.isNew()) {
                saleQueries.transaction {
                    if (model.isOrderedByCustomer) {
                        saleQueries.insert(
                            productId = model.productId,
                            customerId = model.customerId,
                            stockId = model.stockId,
                            quantity = model.quantity.toLong(),
                            purchasePrice = model.purchasePrice.toDouble(),
                            salePrice = model.salePrice.toDouble(),
                            deliveryPrice = model.deliveryPrice.toDouble(),
                            amazonCode = model.amazonCode,
                            amazonRequestNumber = model.amazonRequestNumber,
                            amazonTax = model.amazonTax.toLong(),
                            amazonProfit = model.amazonProfit.toDouble(),
                            amazonSKU = model.amazonSKU,
                            resaleProfit = model.resaleProfit.toDouble(),
                            totalProfit = model.totalProfit.toDouble(),
                            dateOfSale = model.dateOfSale,
                            dateOfPayment = model.dateOfPayment,
                            shippingDate = model.shippingDate,
                            deliveryDate = model.deliveryDate,
                            isOrderedByCustomer = true,
                            isPaidByCustomer = model.isPaidByCustomer,
                            delivered = model.delivered,
                            canceled = model.canceled,
                            isAmazon = model.isAmazon,
                            timestamp = model.timestamp
                        )
                    } else {
                        if (pushed) {
                            val stockItem = stockQueries.getById(
                                id = model.stockId,
                                mapper = ::mapStockItem
                            ).executeAsOne()

                            quantity = stockItem.quantity - model.quantity

                            stockQueries.updateStockQuantity(
                                id = stockItem.id,
                                quantity = quantity.toLong()
                            )
                        }

                        newQuantity = stockQueries.getById(
                            id = model.stockId,
                            mapper = ::mapStockItem
                        ).executeAsOne().quantity

                        saleQueries.insert(
                            productId = model.productId,
                            customerId = model.customerId,
                            stockId = model.stockId,
                            quantity = model.quantity.toLong(),
                            purchasePrice = model.purchasePrice.toDouble(),
                            salePrice = model.salePrice.toDouble(),
                            deliveryPrice = model.deliveryPrice.toDouble(),
                            amazonCode = model.amazonCode,
                            amazonRequestNumber = model.amazonRequestNumber,
                            amazonTax = model.amazonTax.toLong(),
                            amazonProfit = model.amazonProfit.toDouble(),
                            amazonSKU = model.amazonSKU,
                            resaleProfit = model.resaleProfit.toDouble(),
                            totalProfit = model.totalProfit.toDouble(),
                            dateOfSale = model.dateOfSale,
                            dateOfPayment = model.dateOfPayment,
                            shippingDate = model.shippingDate,
                            deliveryDate = model.deliveryDate,
                            isOrderedByCustomer = false,
                            isPaidByCustomer = model.isPaidByCustomer,
                            delivered = model.delivered,
                            canceled = model.canceled,
                            isAmazon = model.isAmazon,
                            timestamp = model.timestamp
                        )
                    }
                    id = saleQueries.getLastId().executeAsOne()

                    versionQueries.insertWithId(
                        id = version.id,
                        name = version.name,
                        timestamp = version.timestamp
                    )

                    versionQueries.insertWithId(
                        id = stockVersion.id,
                        name = stockVersion.name,
                        timestamp = stockVersion.timestamp
                    )

                    onSuccess(id, newQuantity)
                }
            } else {
                saleQueries.transaction {
                    if (model.isOrderedByCustomer) {
                        saleQueries.insertWithId(
                            id = model.id,
                            productId = model.productId,
                            customerId = model.customerId,
                            stockId = model.stockId,
                            quantity = model.quantity.toLong(),
                            purchasePrice = model.purchasePrice.toDouble(),
                            salePrice = model.salePrice.toDouble(),
                            amazonCode = model.amazonCode,
                            amazonRequestNumber = model.amazonRequestNumber,
                            amazonTax = model.amazonTax.toLong(),
                            amazonProfit = model.amazonProfit.toDouble(),
                            amazonSKU = model.amazonSKU,
                            resaleProfit = model.resaleProfit.toDouble(),
                            totalProfit = model.totalProfit.toDouble(),
                            deliveryPrice = model.deliveryPrice.toDouble(),
                            dateOfSale = model.dateOfSale,
                            dateOfPayment = model.dateOfPayment,
                            shippingDate = model.shippingDate,
                            deliveryDate = model.deliveryDate,
                            isOrderedByCustomer = true,
                            isPaidByCustomer = model.isPaidByCustomer,
                            delivered = model.delivered,
                            canceled = model.canceled,
                            isAmazon = model.isAmazon,
                            timestamp = model.timestamp
                        )
                    } else {
                        if (pushed) {
                            val stockItem = stockQueries.getById(
                                id = model.stockId,
                                mapper = ::mapStockItem
                            ).executeAsOne()

                            quantity = stockItem.quantity - model.quantity

                            stockQueries.updateStockQuantity(
                                id = stockItem.id,
                                quantity = quantity.toLong()
                            )
                        }

                        newQuantity = stockQueries.getById(
                            id = model.stockId,
                            mapper = ::mapStockItem
                        ).executeAsOne().quantity

                        saleQueries.insertWithId(
                            id = model.id,
                            productId = model.productId,
                            customerId = model.customerId,
                            stockId = model.stockId,
                            quantity = model.quantity.toLong(),
                            purchasePrice = model.purchasePrice.toDouble(),
                            salePrice = model.salePrice.toDouble(),
                            deliveryPrice = model.deliveryPrice.toDouble(),
                            amazonCode = model.amazonCode,
                            amazonRequestNumber = model.amazonRequestNumber,
                            amazonTax = model.amazonTax.toLong(),
                            amazonProfit = model.amazonProfit.toDouble(),
                            amazonSKU = model.amazonSKU,
                            resaleProfit = model.resaleProfit.toDouble(),
                            totalProfit = model.totalProfit.toDouble(),
                            dateOfSale = model.dateOfSale,
                            dateOfPayment = model.dateOfPayment,
                            shippingDate = model.shippingDate,
                            deliveryDate = model.deliveryDate,
                            isOrderedByCustomer = false,
                            isPaidByCustomer = model.isPaidByCustomer,
                            delivered = model.delivered,
                            canceled = model.canceled,
                            isAmazon = model.isAmazon,
                            timestamp = model.timestamp
                        )
                    }
                    versionQueries.insertWithId(
                        id = version.id,
                        name = version.name,
                        timestamp = version.timestamp
                    )

                    versionQueries.insertWithId(
                        id = stockVersion.id,
                        name = stockVersion.name,
                        timestamp = stockVersion.timestamp
                    )

                    id = model.id
                    onSuccess(model.id, newQuantity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(Errors.INSERT_DATABASE_ERROR)
        }

        return id
    }

    override suspend fun update(
        model: Sale,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (itemQuantity: Int) -> Unit
    ) {
        var newStockQuantity = 0L

        try {
            saleQueries.transaction {
                if (!model.isOrderedByCustomer) {
                    val saleQuantity = saleQueries.getById(
                        id = model.id,
                        mapper = ::mapSale
                    ).executeAsOne().quantity

                    if (saleQuantity != model.quantity) {
                        val stockQuantity =
                            stockQueries.getStockQuantity(id = model.stockId).executeAsOne()
                        newStockQuantity = stockQuantity + (saleQuantity - model.quantity)
                        stockQueries.updateStockQuantity(
                            quantity = newStockQuantity,
                            id = model.stockId
                        )
                    }
                }

                saleQueries.update(
                    productId = model.productId,
                    customerId = model.customerId,
                    stockId = model.stockId,
                    quantity = model.quantity.toLong(),
                    purchasePrice = model.purchasePrice.toDouble(),
                    salePrice = model.salePrice.toDouble(),
                    deliveryPrice = model.deliveryPrice.toDouble(),
                    amazonCode = model.amazonCode,
                    amazonRequestNumber = model.amazonRequestNumber,
                    amazonTax = model.amazonTax.toLong(),
                    amazonProfit = model.amazonProfit.toDouble(),
                    amazonSKU = model.amazonSKU,
                    resaleProfit = model.resaleProfit.toDouble(),
                    totalProfit = model.totalProfit.toDouble(),
                    dateOfSale = model.dateOfSale,
                    dateOfPayment = model.dateOfPayment,
                    shippingDate = model.shippingDate,
                    deliveryDate = model.deliveryDate,
                    isOrderedByCustomer = model.isOrderedByCustomer,
                    isPaidByCustomer = model.isPaidByCustomer,
                    delivered = model.delivered,
                    canceled = model.canceled,
                    isAmazon = model.isAmazon,
                    timestamp = model.timestamp,
                    id = model.id
                )

                versionQueries.update(
                    name = version.name,
                    timestamp = version.timestamp,
                    id = version.id
                )

                onSuccess(newStockQuantity.toInt())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(Errors.UPDATE_DATABASE_ERROR)
        }
    }

    override suspend fun cancelSale(saleId: Long) {
        val sale = saleQueries.getById(id = saleId).executeAsOne()
        saleQueries.setCanceledSale(id = saleId)
        stockQueries.updateStockQuantity(
            id = sale.stockId,
            quantity = stockQueries.getStockQuantity(id = sale.stockId).executeAsOne()
                    + sale.quantity
        )
    }

    override suspend fun getAllSales(): List<Sale> {
        return saleQueries.getAll(mapper = ::mapSale).executeAsList()
    }

    override suspend fun getAllAmazonSales(): List<Sale> {
        return saleQueries.getAmazonSales(mapper = ::mapSale).executeAsList()
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleQueries.getByCustomerId(customerId = customerId, mapper = ::mapSale)
            .asFlow().mapToList(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getOrdersByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getAllOrdersByCustomerNameAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            return saleQueries.getAllOrdersByCustomerNameDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getOrdersBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return if (isOrderedAsc) {
            saleQueries.getAllOrdersBySalePriceAsc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        } else {
            return saleQueries.getAllOrdersBySalePriceDesc(mapper = ::mapSale)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Sale>> {
        return saleQueries.getDeliveries(
            delivered = delivered,
            mapper = ::mapSale
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleQueries.getLastSales(
            offset = offset.toLong(),
            limit = limit.toLong(),
            mapper = ::mapSale
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getAmazonSales(): Flow<List<Sale>> {
        return saleQueries.getAmazonSales(mapper = ::mapSale).asFlow().mapToList(ioDispatcher)
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
            onError(Errors.DELETE_DATABASE_ERROR)
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
        address: String,
        phoneNumber: String,
        quantity: Long,
        purchasePrice: Double,
        salePrice: Double,
        deliveryPrice: Double,
        categories: List<Category>,
        company: String,
        amazonCode: String,
        requestNumber: Long,
        amazonTax: Long,
        amazonProfit: Double,
        amazonSKU: String,
        resaleProfit: Double,
        totalProfit: Double,
        dateOfSale: Long,
        dateOfPayment: Long,
        shippingDate: Long,
        deliveryDate: Long,
        isOrderedByCustomer: Boolean,
        isPaidByCustomer: Boolean,
        delivered: Boolean,
        canceled: Boolean,
        isAmazon: Boolean,
        timestamp: String
    ): Sale {
        return Sale(
            id = id,
            productId = productId,
            customerId = customerId,
            stockId = stockOrderId,
            name = name,
            customerName = customerName,
            photo = photo,
            address = address,
            phoneNumber = phoneNumber,
            quantity = quantity.toInt(),
            purchasePrice = purchasePrice.toFloat(),
            salePrice = salePrice.toFloat(),
            deliveryPrice = deliveryPrice.toFloat(),
            categories = categories,
            company = company,
            amazonCode = amazonCode,
            amazonRequestNumber = requestNumber,
            amazonTax = amazonTax.toInt(),
            amazonProfit = amazonProfit.toFloat(),
            amazonSKU = amazonSKU,
            resaleProfit = resaleProfit.toFloat(),
            totalProfit = totalProfit.toFloat(),
            dateOfSale = dateOfSale,
            dateOfPayment = dateOfPayment,
            shippingDate = shippingDate,
            deliveryDate = deliveryDate,
            isOrderedByCustomer = isOrderedByCustomer,
            isPaidByCustomer = isPaidByCustomer,
            delivered = delivered,
            canceled = canceled,
            isAmazon = isAmazon,
            timestamp = timestamp
        )
    }

    private fun mapStockItem(
        id: Long,
        productId: Long,
        name: String,
        photo: ByteArray,
        date: Long,
        dateOfPayment: Long,
        validity: Long,
        quantity: Long,
        categories: List<Category>,
        company: String,
        purchasePrice: Double,
        salePrice: Double,
        isPaid: Boolean,
        timestamp: String
    ) = StockItem(
        id = id,
        productId = productId,
        name = name,
        photo = photo,
        date = date,
        dateOfPayment = dateOfPayment,
        validity = validity,
        quantity = quantity.toInt(),
        categories = categories,
        company = company,
        purchasePrice = purchasePrice.toFloat(),
        salePrice = salePrice.toFloat(),
        isPaid = isPaid,
        timestamp = timestamp
    )
}