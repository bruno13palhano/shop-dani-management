package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.delivery.DeliveryData
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.stockorder.StockData
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultSaleNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultSaleRepository @Inject constructor(
    @DefaultSaleNet private val saleNetwork: SaleNetwork,
    @InternalSaleLight private val saleData: SaleData,
    @InternalStockOrderLight private val stockData: StockData,
    @InternalDeliveryLight val deliveryData: DeliveryData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleRepository {
    override suspend fun insert(
        model: Sale,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val saleVersion = Versions.saleVersion(timestamp = model.timestamp)

        val id = saleData.insert(model = model, version = saleVersion, onError = onError) {
            val netModel = createSale(
                sale = model,
                saleId = it,
                stockOrderId = model.stockOrderId
            )

            CoroutineScope(ioDispatcher).launch {
                try { saleNetwork.insert(data = netModel) }
                catch (e: Exception) { onError(4) }
            }
        }

        versionData.insert(model = saleVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    versionNetwork.insert(saleVersion)
                    onSuccess(id)
                } catch (e: Exception) { onError(4) }
            }
        }

        return id
    }

    override suspend fun update(
        model: Sale,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val saleVersion = Versions.saleVersion(timestamp = model.timestamp)

        saleData.update(model = model, version = saleVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    saleNetwork.update(data = model)
                    versionNetwork.update(saleVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(5) }
            }
        }
    }

    override suspend fun cancelSale(saleId: Long) {
        saleData.cancelSale(saleId = saleId)
    }

    override suspend fun insertItems(
        sale: Sale,
        stockItem: StockItem,
        delivery: Delivery,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val saleVersion = Versions.saleVersion(timestamp = sale.timestamp)
        val deliveryVersion = Versions.deliveryVersion(timestamp = delivery.timestamp)

        saleData.insertItems(
            sale = sale,
            stockItem = stockItem,
            delivery = delivery,
            saleVersion = saleVersion,
            deliveryVersion = deliveryVersion,
            onError = onError
        ) { saleId, stockOrderId, deliveryId ->
            val netStockOrder = createStockOrder(stockItem = stockItem, id = stockOrderId)
            val netSale = createSale(sale = sale, saleId = saleId, stockOrderId = stockOrderId)
            val netDelivery = createDelivery(
                delivery = delivery,
                deliveryId = deliveryId,
                saleId = saleId
            )

            CoroutineScope(ioDispatcher).launch {
                try {
                    saleNetwork.insertItems(netSale, netStockOrder, netDelivery)
                    versionNetwork.insert(saleVersion)
                    versionNetwork.insert(deliveryVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(4) }
            }
        }
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

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val saleVersion = Versions.saleVersion(timestamp = timestamp)

        saleData.deleteById(id = id, version = saleVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    saleNetwork.delete(id = id)
                    versionNetwork.update(saleVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(6) }
            }
        }
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
                val items = getDataList(stockData)
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
                deleteIds.forEach { saleData.deleteById(it, netVersion, {}) {} }
                saveList.forEach { saleData.insert(it, netVersion, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )

    override fun getDebitSales(): Flow<List<Sale>> {
        return saleData.getDebitSales()
    }

    private fun createSale(sale: Sale, saleId: Long, stockOrderId: Long) = Sale(
        id = saleId,
        productId = sale.productId,
        stockOrderId = stockOrderId,
        customerId = sale.customerId,
        name = sale.name,
        customerName = sale.customerName,
        photo = sale.photo,
        quantity = sale.quantity,
        purchasePrice = sale.purchasePrice,
        salePrice = sale.salePrice,
        deliveryPrice = sale.deliveryPrice,
        categories = sale.categories,
        company = sale.company,
        dateOfSale = sale.dateOfSale,
        dateOfPayment = sale.dateOfPayment,
        isOrderedByCustomer = sale.isOrderedByCustomer,
        isPaidByCustomer = sale.isPaidByCustomer,
        canceled = sale.canceled,
        timestamp = sale.timestamp
    )

    private fun createStockOrder(stockItem: StockItem, id: Long) = StockItem(
        id = id,
        productId = stockItem.productId,
        name = stockItem.name,
        photo = stockItem.photo,
        date = stockItem.date,
        validity = stockItem.validity,
        quantity = stockItem.quantity,
        categories = stockItem.categories,
        company = stockItem.company,
        purchasePrice = stockItem.purchasePrice,
        salePrice = stockItem.salePrice,
        isPaid = stockItem.isPaid,
        timestamp = stockItem.timestamp
    )

    private fun createDelivery(delivery: Delivery, deliveryId: Long, saleId: Long) = Delivery(
        id = deliveryId,
        saleId = saleId,
        customerName = delivery.customerName,
        address = delivery.address,
        phoneNumber = delivery.phoneNumber,
        productName = delivery.productName,
        price = delivery.price,
        deliveryPrice = delivery.deliveryPrice,
        shippingDate = delivery.shippingDate,
        deliveryDate = delivery.deliveryDate,
        delivered = delivery.delivered,
        timestamp = delivery.timestamp
    )
}