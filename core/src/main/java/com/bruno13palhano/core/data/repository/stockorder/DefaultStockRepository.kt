package com.bruno13palhano.core.data.repository.stockorder

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.network.access.StockOrderNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultStockOrderNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultStockRepository @Inject constructor(
    @DefaultStockOrderNet private val stockOrderNetwork: StockOrderNetwork,
    @InternalStockOrderLight private val stockData: StockData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockRepository {
    override suspend fun insert(
        model: StockItem,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = model.timestamp)

        val id = stockData.insert(
            model = model,
            version = stockOrderVersion,
            onError = onError,
        ) {
            val netModel = StockItem(
                id = it,
                productId = model.productId,
                name = model.name,
                photo = model.photo,
                date = model.date,
                validity = model.validity,
                quantity = model.quantity,
                categories = model.categories,
                company = model.company,
                purchasePrice = model.purchasePrice,
                salePrice = model.salePrice,
                isPaid = model.isPaid,
                timestamp = model.timestamp
            )

            CoroutineScope(ioDispatcher).launch {
                try {
                    stockOrderNetwork.insert(data = netModel)
                    versionNetwork.insert(data = stockOrderVersion)
                    onSuccess(netModel.id)
                }
                catch (e: Exception) { onError(4) }
            }
        }

        return id
    }

    override suspend fun update(
        model: StockItem,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = model.timestamp)

        stockData.update(model = model, version = stockOrderVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    stockOrderNetwork.update(data = model)
                    versionNetwork.insert(data = stockOrderVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(5) }
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

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int, timestamp: String) {
        stockData.updateStockOrderQuantity(id = id, quantity = quantity)
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = timestamp)

        stockData.deleteById(id = id, version = stockOrderVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    stockOrderNetwork.delete(id = id)
                    versionNetwork.update(data = stockOrderVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(6) }
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

    override fun getStockOrderItems(): Flow<List<StockItem>> {
        return stockData.getStockOrderItems()
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
            dataVersion = getDataVersion(versionData, 3L),
            networkVersion = getNetworkVersion(versionNetwork, 3L),
            dataList = getDataList(stockData),
            networkList = getNetworkList(stockOrderNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { stockOrderNetwork.delete(it) }
                saveList.forEach { stockOrderNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { stockData.deleteById(it, netVersion, {}) {} }
                saveList.forEach { stockData.insert(it, netVersion, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )
}