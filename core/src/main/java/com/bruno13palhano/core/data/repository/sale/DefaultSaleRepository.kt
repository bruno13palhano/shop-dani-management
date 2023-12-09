package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultSaleRepository @Inject constructor(
    @DefaultSaleNet private val saleNetwork: SaleNetwork,
    @InternalSaleLight private val saleData: SaleData,
    @InternalStockOrderLight private val stockOrderData: StockOrderData,
    @InternalDeliveryLight val deliveryData: DeliveryData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleRepository {
    override suspend fun insert(model: Sale): Long {
        val saleVersion = DataVersion(4L, "SALE", model.timestamp)
        val id = saleData.insert(model = model) {
            CoroutineScope(ioDispatcher).launch {
                val netModel = Sale(
                    id = it,
                    productId = model.productId,
                    stockOrderId = model.stockOrderId,
                    customerId = model.customerId,
                    name = model.name,
                    customerName = model.customerName,
                    photo = model.photo,
                    quantity = model.quantity,
                    purchasePrice = model.purchasePrice,
                    salePrice = model.salePrice,
                    deliveryPrice = model.deliveryPrice,
                    categories = model.categories,
                    company = model.company,
                    dateOfSale = model.dateOfSale,
                    dateOfPayment =model.dateOfPayment,
                    isOrderedByCustomer = model.isOrderedByCustomer,
                    isPaidByCustomer = model.isPaidByCustomer,
                    canceled = model.canceled,
                    timestamp = model.timestamp
                )

                saleNetwork.insert(data = netModel)
            }
        }
        versionData.insert(saleVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.insert(saleVersion)
            }
        }

        return id
    }

    override suspend fun update(model: Sale) {
        val saleVersion = DataVersion(4L, "SALE", model.timestamp)
        saleData.update(model = model) {
            CoroutineScope(ioDispatcher).launch {
                saleNetwork.update(data = model)
            }
        }
        versionData.update(saleVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.update(saleVersion)
            }
        }
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
        val saleVersion = DataVersion(4L, "SALE", sale.timestamp)
        val deliveryVersion = DataVersion(5L, "DELIVERY", sale.timestamp)
        saleData.insertItems(
            sale = sale,
            stockOrder = stockOrder,
            delivery = delivery
        ) { saleId, stockOrderId, deliveryId ->
            onSuccess()
            CoroutineScope(ioDispatcher).launch {
                val netSale = Sale(
                    id = saleId,
                    productId = sale.productId,
                    stockOrderId = sale.stockOrderId,
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
                val netStockOrder = StockOrder(
                    id = stockOrderId,
                    productId = stockOrder.productId,
                    name = stockOrder.name,
                    photo = stockOrder.photo,
                    date = stockOrder.date,
                    validity = stockOrder.validity,
                    quantity = stockOrder.quantity,
                    categories = stockOrder.categories,
                    company = stockOrder.company,
                    purchasePrice = stockOrder.purchasePrice,
                    salePrice = stockOrder.salePrice,
                    isOrderedByCustomer = stockOrder.isOrderedByCustomer,
                    isPaid = stockOrder.isPaid,
                    timestamp = stockOrder.timestamp
                )
                val netDelivery = Delivery(
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
                saleNetwork.insertItems(netSale, netStockOrder, netDelivery)
            }
        }
        versionData.insert(saleVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.insert(saleVersion)
            }
        }
        versionData.insert(deliveryVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.insert(deliveryVersion)
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

    override suspend fun deleteById(id: Long, timestamp: String) {
        val saleVersion = DataVersion(4L, "SALE", timestamp)
        saleData.deleteById(id = id) {
            CoroutineScope(ioDispatcher).launch {
                saleNetwork.delete(id = id)
            }
        }
        versionData.update(saleVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.update(saleVersion)
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
                deleteIds.forEach { saleData.deleteById(it) {} }
                saveList.forEach { saleData.insert(it) {} }
                versionData.insert(netVersion) {}
            }
        )

    override fun getDebitSales(): Flow<List<Sale>> {
        return saleData.getDebitSales()
    }
}