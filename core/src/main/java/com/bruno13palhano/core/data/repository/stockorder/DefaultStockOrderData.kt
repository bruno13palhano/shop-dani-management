package com.bruno13palhano.core.data.repository.stockorder

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.StockOrderTableQueries
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class DefaultStockOrderData @Inject constructor(
    private val stockOrderQueries: StockOrderTableQueries,
    private val versionQueries: VersionTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockOrderData {
    override suspend fun insert(
        model: StockOrder,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        var id = 0L

        try {
            if (model.isNew()) {
                stockOrderQueries.transaction {
                    stockOrderQueries.insert(
                        productId = model.productId,
                        date = model.date,
                        validity = model.validity,
                        quantity = model.quantity.toLong(),
                        purchasePrice = model.purchasePrice.toDouble(),
                        salePrice = model.salePrice.toDouble(),
                        isOrderedByCustomer = model.isOrderedByCustomer,
                        isPaid = model.isPaid,
                        timestamp = model.timestamp
                    )
                    id = stockOrderQueries.lastId().executeAsOne()

                    versionQueries.insertWithId(
                        id = version.id,
                        name = version.name,
                        timestamp = version.timestamp
                    )

                    onSuccess(id)
                }
            } else {
                stockOrderQueries.transaction {
                    stockOrderQueries.insertWithId(
                        id = model.id,
                        productId = model.productId,
                        date = model.date,
                        validity = model.validity,
                        quantity = model.quantity.toLong(),
                        purchasePrice = model.purchasePrice.toDouble(),
                        salePrice = model.salePrice.toDouble(),
                        isOrderedByCustomer = model.isOrderedByCustomer,
                        isPaid = model.isPaid,
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
        model: StockOrder,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            stockOrderQueries.transaction {
                stockOrderQueries.update(
                    id = model.id,
                    productId = model.productId,
                    date = model.date,
                    validity = model.validity,
                    quantity = model.quantity.toLong(),
                    purchasePrice = model.purchasePrice.toDouble(),
                    salePrice = model.salePrice.toDouble(),
                    isOrderedByCustomer = model.isOrderedByCustomer,
                    isPaid = model.isPaid,
                    timestamp = model.timestamp
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

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.getItems(
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.search(
            name = value,
            company = value,
            description = value,
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return stockOrderQueries.getByCategory(
            category = category,
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        stockOrderQueries.updateStockOrderQuantity(quantity = quantity.toLong(), id = id)
    }

    override suspend fun deleteById(
        id: Long,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            stockOrderQueries.transaction {
                stockOrderQueries.delete(id = id)

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

    override fun getAll(): Flow<List<StockOrder>> {
        return stockOrderQueries.getAll(mapper = ::mapStockOrder)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getDebitStock(): Flow<List<StockOrder>> {
        return stockOrderQueries.getDebitStock(mapper = ::mapStockOrder)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getOutOfStock(): Flow<List<StockOrder>> {
        return stockOrderQueries.getOutOfStock(mapper = ::mapStockOrder)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.getStockOrderItems(
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedAsc) {
            stockOrderQueries.getDebitStockByPriceAsc(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        } else {
            stockOrderQueries.getDebitStockByPriceDesc(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedAsc) {
            stockOrderQueries.getDebitStockByNameAsc(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        } else {
            stockOrderQueries.getDebitStockByNameDesc(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderQueries.getById(id = id, mapper = ::mapStockOrder)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderQueries.getLast(mapper = ::mapStockOrder)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    private fun mapStockOrder(
        id: Long,
        productId: Long,
        name: String,
        photo: ByteArray,
        date: Long,
        validity: Long,
        quantity: Long,
        categories: List<Category>,
        company: String,
        purchasePrice: Double,
        salePrice: Double,
        isOrderedByCustomer: Boolean,
        isPaid: Boolean,
        timestamp: String
    ) = StockOrder(
        id = id,
        productId = productId,
        name = name,
        photo = photo,
        date = date,
        validity = validity,
        quantity = quantity.toInt(),
        categories = categories,
        company = company,
        purchasePrice = purchasePrice.toFloat(),
        salePrice = salePrice.toFloat(),
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid,
        timestamp = timestamp
    )
}