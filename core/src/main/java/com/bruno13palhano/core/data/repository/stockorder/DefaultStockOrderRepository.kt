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
import com.bruno13palhano.core.model.StockOrder
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

internal class DefaultStockOrderRepository @Inject constructor(
    @DefaultStockOrderNet private val stockOrderNetwork: StockOrderNetwork,
    @InternalStockOrderLight private val stockOrderData: StockOrderData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockOrderRepository {
    override suspend fun insert(
        model: StockOrder,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = model.timestamp)

        val id = stockOrderData.insert(
            model = model,
            onError = onError,
        ) {
            val netModel = StockOrder(
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
                isOrderedByCustomer = model.isOrderedByCustomer,
                isPaid = model.isPaid,
                timestamp = model.timestamp
            )

            try {
                CoroutineScope(ioDispatcher).launch {
                    stockOrderNetwork.insert(data = netModel)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(4)
            }
        }

        versionData.insert(model = stockOrderVersion, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    versionNetwork.insert(data = stockOrderVersion)
                    onSuccess(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(4)
            }
        }

        return id
    }

    override suspend fun update(
        model: StockOrder,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = model.timestamp)

        stockOrderData.update(model = model, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    stockOrderNetwork.update(data = model)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(5)
            }
        }

        versionData.update(model = stockOrderVersion, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    versionNetwork.insert(data = stockOrderVersion)
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(5)
            }
        }
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getItems(isOrderedByCustomer = isOrderedByCustomer)
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.search(value = value, isOrderedByCustomer = isOrderedByCustomer)
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return stockOrderData.getByCategory(
            category = category,
            isOrderedByCustomer = isOrderedByCustomer
        )
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int, timestamp: String) {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = timestamp)
        stockOrderData.updateStockOrderQuantity(id = id, quantity = quantity)
        versionData.update(
            model = stockOrderVersion,
            onError = {}
        ) {}
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val stockOrderVersion = Versions.stockOrderVersion(timestamp = timestamp)

        stockOrderData.deleteById(id = id, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    stockOrderNetwork.delete(id = id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(6)
            }
        }

        versionData.update(model = stockOrderVersion, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    versionNetwork.update(data = stockOrderVersion)
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(6)
            }
        }
    }

    override fun getAll(): Flow<List<StockOrder>> {
        return stockOrderData.getAll()
    }

    override fun getDebitStock(): Flow<List<StockOrder>> {
        return stockOrderData.getDebitStock()
    }

    override fun getOutOfStock(): Flow<List<StockOrder>> {
        return stockOrderData.getOutOfStock()
    }

    override fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getStockOrderItems(isOrderedByCustomer)
    }

    override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getDebitStockByPrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getDebitStockByName(isOrderedAsc = isOrderedAsc)
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderData.getById(id = id)
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderData.getLast()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.syncData(
            dataVersion = getDataVersion(versionData, 3L),
            networkVersion = getNetworkVersion(versionNetwork, 3L),
            dataList = getDataList(stockOrderData),
            networkList = getNetworkList(stockOrderNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { stockOrderNetwork.delete(it) }
                saveList.forEach { stockOrderNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { stockOrderData.deleteById(it, {}) {} }
                saveList.forEach { stockOrderData.insert(it, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )
}