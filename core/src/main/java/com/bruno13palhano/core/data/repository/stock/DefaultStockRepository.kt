package com.bruno13palhano.core.data.repository.stock

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalStockLight
import com.bruno13palhano.core.data.di.InternalVersionLight
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
import com.bruno13palhano.core.network.access.StockNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultStockNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultStockRepository @Inject constructor(
    @DefaultStockNet private val stockNetwork: StockNetwork,
    @InternalStockLight private val stockData: StockData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockRepository {
    override suspend fun insert(
        model: StockItem,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val stockOrderVersion = Versions.stockVersion(timestamp = model.timestamp)

        val id = stockData.insert(
            model = model,
            version = stockOrderVersion,
            onError = onError,
        ) {
            val netModel = stockItemToStockItemNet(
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
                    stockNetwork.insert(data = netModel)
                    versionNetwork.insert(data = versionToVersionNet(stockOrderVersion))
                    onSuccess(netModel.id)
                }
                catch (e: Exception) { onError(Errors.INSERT_SERVER_ERROR) }
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
                    stockNetwork.update(data = stockItemToStockItemNet(model))
                    versionNetwork.insert(data = versionToVersionNet(stockOrderVersion))
                    onSuccess()
                }
                catch (e: Exception) { onError(Errors.UPDATE_SERVER_ERROR) }
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

    override suspend fun updateStockQuantity(id: Long, quantity: Int, timestamp: String) {
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
                    stockNetwork.delete(id = id)
                    versionNetwork.update(data = versionToVersionNet(stockOrderVersion))
                    onSuccess()
                }
                catch (e: Exception) { onError(Errors.DELETE_SERVER_ERROR) }
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
            dataVersion = getDataVersion(versionData, Versions.stockVersionId),
            networkVersion = getNetworkVersion(versionNetwork, Versions.stockVersionId),
            dataList = getDataList(stockData),
            networkList = getNetworkList(stockNetwork).map { stockItemNetToStockItem(it) },
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { stockNetwork.delete(it) }
                saveList.forEach { stockNetwork.insert(stockItemToStockItemNet(it)) }
                versionNetwork.insert(versionToVersionNet(dtVersion))
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { stockData.deleteById(it, netVersion, {}) {} }
                saveList.forEach { stockData.insert(it, netVersion, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )
}