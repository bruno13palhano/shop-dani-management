package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.delivery.DeliveryData
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.stockorder.StockOrderData
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultSaleNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

internal class DefaultSaleRepository @Inject constructor(
    @DefaultSaleNet private val saleNetwork: SaleNetwork,
    @InternalSaleLight private val saleData: SaleData,
    @InternalStockOrderLight private val stockOrderData: StockOrderData,
    @InternalDeliveryLight val deliveryData: DeliveryData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
) : SaleRepository {
    override suspend fun insert(model: Sale): Long {
        versionData.insert(DataVersion(4L, "SALE", model.timestamp))
        return saleData.insert(model = model)
    }

    override suspend fun update(model: Sale) {
        versionData.update(DataVersion(4L, "SALE", model.timestamp))
        saleData.update(model = model)
    }

    override suspend fun cancelSale(saleId: Long) {
        saleData.cancelSale(saleId = saleId)
    }

    override suspend fun insertItems(
        sale: Sale,
        stockOrder: StockOrder,
        delivery: Delivery,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        versionData.insert(DataVersion(4L, "SALE", sale.timestamp))
        versionData.insert(DataVersion(5L, "DELIVERY", sale.timestamp))
        saleData.insertItems(
            sale = sale,
            stockOrder = stockOrder,
            delivery = delivery,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleData.getByCustomerId(customerId = customerId)
    }

    override fun getLastSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getLastSales(offset = offset, limit = limit)
    }

    override fun getAllStockSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getAllStockSales(offset = offset, limit = limit)
    }

    override fun getAllOrdersSales(offset: Int, limit: Int): Flow<List<Sale>> {
        return saleData.getAllOrdersSales(offset = offset, limit = limit)
    }

    override suspend fun deleteById(id: Long) {
        versionData.update(DataVersion(4L, "SALE", OffsetDateTime.now()))
        saleData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Sale>> {
        return saleData.getAll()
    }

    override fun getAllCanceledSales(): Flow<List<Sale>> {
        return saleData.getAllCanceledSales()
    }

    override fun getCanceledByName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getCanceledByName(isOrderedAsc = isOrderedAsc)
    }

    override fun getCanceledByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getCanceledByCustomerName(isOrderedAsc = isOrderedAsc)
    }

    override fun getCanceledByPrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getCanceledByPrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getSalesByCustomerName(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return saleData.getSalesByCustomerName(
            isPaidByCustomer = isPaidByCustomer,
            isOrderedAsc = isOrderedAsc
        )
    }

    override fun getSalesBySalePrice(
        isPaidByCustomer: Boolean,
        isOrderedAsc: Boolean
    ): Flow<List<Sale>> {
        return saleData.getSalesBySalePrice(
            isPaidByCustomer = isPaidByCustomer,
            isOrderedAsc = isOrderedAsc
        )
    }

    override fun getAllSalesByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getAllSalesByCustomerName(isOrderedAsc = isOrderedAsc)
    }

    override fun getAllSalesBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getAllSalesBySalePrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getById(id: Long): Flow<Sale> {
        return saleData.getById(id = id)
    }

    override fun getLast(): Flow<Sale> {
        return saleData.getLast()
    }

    override suspend fun syncWith(synchronizer: Synchronizer) =
        synchronizer.syncData(
            dataVersion = getDataVersion(versionData, 4L),
            networkVersion = getNetworkVersion(versionNetwork, 4L),
            dataList = getDataList(saleData),
            networkList = getNetworkList(saleNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                val items = getDataList(stockOrderData)
                val deliveries = getDataList(deliveryData)
                deleteIds.forEach { saleNetwork.delete(it) }
                saveList.forEach { sale ->
                    items.forEach { item ->
                        deliveries.forEach { delivery ->
                            if (sale.stockOrderId == item.id && sale.id == delivery.saleId) {
                                saleNetwork.insertItems(sale, item, delivery)
                            }
                        }
                    }
                }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { saleData.deleteById(it) }
                saveList.forEach { saleData.insert(it) }
                versionData.insert(netVersion)
            }
        )

    override fun getDebitSales(): Flow<List<Sale>> {
        return saleData.getDebitSales()
    }
}