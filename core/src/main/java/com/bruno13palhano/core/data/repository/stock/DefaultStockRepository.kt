package com.bruno13palhano.core.data.repository.stock

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalStock
import com.bruno13palhano.core.data.di.InternalVersion
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.stockItemNetToStockItem
import com.bruno13palhano.core.data.repository.stockItemToStockItemNet
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.data.repository.versionToVersionNet
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.network.access.RemoteStockData
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.di.FirebaseStock
import com.bruno13palhano.core.network.di.FirebaseVersion
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultStockRepository
    @Inject
    constructor(
        @FirebaseStock private val remoteStockData: RemoteStockData,
        @InternalStock private val stockData: StockData,
        @InternalVersion private val versionData: VersionData,
        @FirebaseVersion private val remoteVersionData: RemoteVersionData,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : StockRepository {
        override suspend fun insert(
            model: StockItem,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit
        ): Long {
            val stockOrderVersion = Versions.stockVersion(timestamp = model.timestamp)

            val id =
                stockData.insert(
                    model = model,
                    version = stockOrderVersion,
                    onError = onError
                ) {
                    val netModel =
                        stockItemToStockItemNet(
                            StockItem(
                                id = it,
                                productId = model.productId,
                                name = model.name,
                                photo = model.photo,
                                date = model.date,
                                dateOfPayment = model.dateOfPayment,
                                validity = model.validity,
                                quantity = model.quantity,
                                categories = model.categories,
                                company = model.company,
                                purchasePrice = model.purchasePrice,
                                salePrice = model.salePrice,
                                isPaid = model.isPaid,
                                timestamp = model.timestamp
                            )
                        )

                    CoroutineScope(ioDispatcher).launch {
                        try {
                            remoteStockData.insert(data = netModel)
                            remoteVersionData.insert(data = versionToVersionNet(stockOrderVersion))
                            onSuccess(netModel.id)
                        } catch (e: Exception) {
                            onError(Errors.INSERT_SERVER_ERROR)
                        }
                    }
                }

            return id
        }

        override suspend fun update(
            model: StockItem,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            val stockOrderVersion = Versions.stockVersion(timestamp = model.timestamp)

            stockData.update(model = model, version = stockOrderVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteStockData.update(data = stockItemToStockItemNet(model))
                        remoteVersionData.insert(data = versionToVersionNet(stockOrderVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.UPDATE_SERVER_ERROR)
                    }
                }
            }
        }

        override fun getItems(): Flow<List<StockItem>> {
            return stockData.getItems()
        }

        override fun search(value: String): Flow<List<StockItem>> {
            return stockData.search(value = value)
        }

        override fun getByCategory(category: String): Flow<List<StockItem>> {
            return stockData.getByCategory(category = category)
        }

        override fun getByCode(code: String): Flow<List<StockItem>> {
            return stockData.getByCode(code = code)
        }

        override suspend fun updateStockQuantity(
            id: Long,
            quantity: Int,
            timestamp: String
        ) {
            stockData.updateStockQuantity(id = id, quantity = quantity)
        }

        override suspend fun deleteById(
            id: Long,
            timestamp: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            val stockOrderVersion = Versions.stockVersion(timestamp = timestamp)

            stockData.deleteById(id = id, version = stockOrderVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteStockData.delete(id = id)
                        remoteVersionData.update(data = versionToVersionNet(stockOrderVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.DELETE_SERVER_ERROR)
                    }
                }
            }
        }

        override fun getAll(): Flow<List<StockItem>> {
            return stockData.getAll()
        }

        override fun getDebitStock(): Flow<List<StockItem>> {
            return stockData.getDebitStock()
        }

        override fun getOutOfStock(): Flow<List<StockItem>> {
            return stockData.getOutOfStock()
        }

        override fun getStockItems(): Flow<List<StockItem>> {
            return stockData.getStockItems()
        }

        override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>> {
            return stockData.getDebitStockByPrice(isOrderedAsc = isOrderedAsc)
        }

        override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>> {
            return stockData.getDebitStockByName(isOrderedAsc = isOrderedAsc)
        }

        override fun getById(id: Long): Flow<StockItem> {
            return stockData.getById(id = id)
        }

        override fun getLast(): Flow<StockItem> {
            return stockData.getLast()
        }

        override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
            synchronizer.syncData(
                dataVersion = getDataVersion(versionData, Versions.STOCK_VERSION_ID),
                networkVersion = getNetworkVersion(remoteVersionData, Versions.STOCK_VERSION_ID),
                dataList = getDataList(stockData),
                networkList = getNetworkList(remoteStockData).map { stockItemNetToStockItem(it) },
                onPush = { deleteIds, saveList, dtVersion ->
                    deleteIds.forEach { remoteStockData.delete(it) }
                    saveList.forEach { remoteStockData.insert(stockItemToStockItemNet(it)) }
                    remoteVersionData.insert(versionToVersionNet(dtVersion))
                },
                onPull = { deleteIds, saveList, netVersion ->
                    deleteIds.forEach { stockData.deleteById(it, netVersion, {}) {} }
                    saveList.forEach { stockData.insert(it, netVersion, {}) {} }
                    versionData.insert(netVersion, {}) {}
                }
            )
    }