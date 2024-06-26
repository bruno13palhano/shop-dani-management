package com.bruno13palhano.core.data.repository.stock

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.StockTableQueries
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class LocalStockData
    @Inject
    constructor(
        private val stockOrderQueries: StockTableQueries,
        private val versionQueries: VersionTableQueries,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : StockData {
        override suspend fun insert(
            model: StockItem,
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
                            dateOfPayment = model.dateOfPayment,
                            validity = model.validity,
                            quantity = model.quantity.toLong(),
                            purchasePrice = model.purchasePrice.toDouble(),
                            salePrice = model.salePrice.toDouble(),
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
                            dateOfPayment = model.dateOfPayment,
                            validity = model.validity,
                            quantity = model.quantity.toLong(),
                            purchasePrice = model.purchasePrice.toDouble(),
                            salePrice = model.salePrice.toDouble(),
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
                onError(Errors.INSERT_DATABASE_ERROR)
            }

            return id
        }

        override suspend fun update(
            model: StockItem,
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
                        dateOfPayment = model.dateOfPayment,
                        validity = model.validity,
                        quantity = model.quantity.toLong(),
                        purchasePrice = model.purchasePrice.toDouble(),
                        salePrice = model.salePrice.toDouble(),
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
                onError(Errors.UPDATE_DATABASE_ERROR)
            }
        }

        override fun getItems(): Flow<List<StockItem>> {
            return stockOrderQueries.getItems(mapper = ::mapStockOrder).asFlow().mapToList(ioDispatcher)
        }

        override fun getByCode(code: String): Flow<List<StockItem>> {
            return stockOrderQueries.getByCode(code = code, mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        }

        override fun search(value: String): Flow<List<StockItem>> {
            return stockOrderQueries.search(
                name = value,
                company = value,
                description = value,
                mapper = ::mapStockOrder
            ).asFlow().mapToList(ioDispatcher)
        }

        override fun getByCategory(category: String): Flow<List<StockItem>> {
            return stockOrderQueries.getByCategory(
                category = category,
                mapper = ::mapStockOrder
            ).asFlow().mapToList(ioDispatcher)
        }

        override suspend fun updateStockQuantity(
            id: Long,
            quantity: Int
        ) {
            stockOrderQueries.updateStockQuantity(quantity = quantity.toLong(), id = id)
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
                onError(Errors.DELETE_DATABASE_ERROR)
            }
        }

        override fun getAll(): Flow<List<StockItem>> {
            return stockOrderQueries.getAll(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        }

        override fun getDebitStock(): Flow<List<StockItem>> {
            return stockOrderQueries.getDebitStock(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        }

        override fun getOutOfStock(): Flow<List<StockItem>> {
            return stockOrderQueries.getOutOfStock(mapper = ::mapStockOrder)
                .asFlow().mapToList(ioDispatcher)
        }

        override fun getStockItems(): Flow<List<StockItem>> {
            return stockOrderQueries.getStockItems(
                mapper = ::mapStockOrder
            ).asFlow().mapToList(ioDispatcher)
        }

        override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>> {
            return if (isOrderedAsc) {
                stockOrderQueries.getDebitStockByPriceAsc(mapper = ::mapStockOrder)
                    .asFlow().mapToList(ioDispatcher)
            } else {
                stockOrderQueries.getDebitStockByPriceDesc(mapper = ::mapStockOrder)
                    .asFlow().mapToList(ioDispatcher)
            }
        }

        override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>> {
            return if (isOrderedAsc) {
                stockOrderQueries.getDebitStockByNameAsc(mapper = ::mapStockOrder)
                    .asFlow().mapToList(ioDispatcher)
            } else {
                stockOrderQueries.getDebitStockByNameDesc(mapper = ::mapStockOrder)
                    .asFlow().mapToList(ioDispatcher)
            }
        }

        override fun getById(id: Long): Flow<StockItem> {
            return stockOrderQueries.getById(id = id, mapper = ::mapStockOrder)
                .asFlow().mapToOne(ioDispatcher)
                .catch { it.printStackTrace() }
        }

        override fun getLast(): Flow<StockItem> {
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