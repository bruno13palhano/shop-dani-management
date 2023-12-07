package com.bruno13palhano.core.data.repository.stockorder

import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.network.access.StockOrderNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultStockOrderNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

internal class StockOrderRepository @Inject constructor(
    @DefaultStockOrderNet private val stockOrderNetwork: StockOrderNetwork,
    @InternalStockOrderLight private val stockOrderData: StockOrderData<StockOrder>,
    @InternalVersionLight private val versionLight: VersionData<DataVersion>,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
) : StockOrderData<StockOrder> {
    override suspend fun insert(model: StockOrder): Long {
        versionLight.insert(DataVersion(3L, "STOCK_ORDER", model.timestamp))
        return stockOrderData.insert(model = model)
    }

    override suspend fun update(model: StockOrder) {
        versionLight.update(DataVersion(3L, "STOCK_ORDER", model.timestamp))
        stockOrderData.update(model = model)
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

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        versionLight.update(DataVersion(3L, "STOCK_ORDER", OffsetDateTime.now()))
        stockOrderData.updateStockOrderQuantity(id = id, quantity = quantity)
    }

    override suspend fun deleteById(id: Long) {
        versionLight.update(DataVersion(3L, "STOCK_ORDER", OffsetDateTime.now()))
        stockOrderData.deleteById(id = id)
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
            dataVersion = getDataVersion(versionLight, 3L),
            networkVersion = getNetworkVersion(versionNetwork, 3L),
            dataList = getDataList(stockOrderData),
            networkList = getNetworkList(stockOrderNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { stockOrderNetwork.delete(it) }
                saveList.forEach { stockOrderNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { stockOrderData.deleteById(it) }
                saveList.forEach { stockOrderData.insert(it) }
                versionLight.insert(netVersion)
            }
        )
}