package com.bruno13palhano.core.data.repository.sale

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalDefaultExcelSheet
import com.bruno13palhano.core.data.di.InternalSaleLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.ExcelSheet
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.mapSalesToSheet
import com.bruno13palhano.core.data.repository.salesSheetHeaders
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.access.StockNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultSaleNet
import com.bruno13palhano.core.network.di.DefaultStockNet
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
    @DefaultStockNet private val stockNetwork: StockNetwork,
    @InternalSaleLight private val saleData: SaleData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @InternalDefaultExcelSheet private val excelSheet: ExcelSheet,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SaleRepository {
    override suspend fun insert(
        model: Sale,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val saleVersion = Versions.saleVersion(timestamp = model.timestamp)
        val stockVersion = Versions.stockVersion(timestamp = model.timestamp)

        val id = saleData.insert(
            model = model,
            version = saleVersion,
            pushed = true,
            onError = onError
        ) { saleId, quantity ->
            val netModel = createSale(
                sale = model,
                saleId = saleId,
                stockOrderId = model.stockId
            )

            CoroutineScope(ioDispatcher).launch {
                try {
                    saleNetwork.insert(data = netModel)
                    versionNetwork.insert(data = saleVersion)
                    if (!netModel.isOrderedByCustomer) {
                        stockNetwork.updateItemQuantity(id = netModel.stockId, quantity = quantity)
                        versionNetwork.insert(data = stockVersion)
                    }
                    onSuccess(netModel.id)
                }
                catch (e: Exception) { onError(Errors.INSERT_SERVER_ERROR) }
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
        val stockVersion = Versions.stockVersion(timestamp = model.timestamp)

        saleData.update(model = model, version = saleVersion, onError = onError) { newItemQuantity ->
            CoroutineScope(ioDispatcher).launch {
                try {
                    saleNetwork.update(data = model)
                    versionNetwork.update(saleVersion)
                    if (!model.isOrderedByCustomer) {
                        stockNetwork.updateItemQuantity(id = model.stockId, quantity = newItemQuantity)
                        versionNetwork.update(stockVersion)
                    }
                    onSuccess()
                }
                catch (e: Exception) { onError(Errors.UPDATE_SERVER_ERROR) }
            }
        }
    }

    override suspend fun cancelSale(saleId: Long) {
        saleData.cancelSale(saleId = saleId)
    }

    override suspend fun exportExcelSheet(sheetName: String) {
        CoroutineScope(ioDispatcher).launch {
            val sales = saleData.getAllSales()
            val salesToSheet = mutableListOf<List<String>>()

            sales.forEach { salesToSheet.add(mapSalesToSheet(it)) }

            excelSheet.createExcel(
                sheetName = sheetName,
                headers = salesSheetHeaders,
                data = salesToSheet
            )
        }
    }

    override fun getByCustomerId(customerId: Long): Flow<List<Sale>> {
        return saleData.getByCustomerId(customerId = customerId)
    }

    override fun getOrdersByCustomerName(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getOrdersByCustomerName(isOrderedAsc = isOrderedAsc)
    }

    override fun getOrdersBySalePrice(isOrderedAsc: Boolean): Flow<List<Sale>> {
        return saleData.getOrdersBySalePrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Sale>> {
        return saleData.getDeliveries(delivered = delivered)
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
                catch (e: Exception) { onError(Errors.DELETE_SERVER_ERROR) }
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
            dataVersion = getDataVersion(versionData, Versions.saleVersionId),
            networkVersion = getNetworkVersion(versionNetwork, Versions.saleVersionId),
            dataList = getDataList(saleData),
            networkList = getNetworkList(saleNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { saleNetwork.delete(it) }
                saveList.forEach { saleNetwork.insert(it)}
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { saleData.deleteById(it, netVersion, {}) {} }
                saveList.forEach { saleData.insert(it, netVersion, false, {}) { _, _ -> } }
                versionData.insert(netVersion, {}) {}
            }
        )

    override fun getDebitSales(): Flow<List<Sale>> {
        return saleData.getDebitSales()
    }

    private fun createSale(sale: Sale, saleId: Long, stockOrderId: Long) = Sale(
        id = saleId,
        productId = sale.productId,
        stockId = stockOrderId,
        customerId = sale.customerId,
        name = sale.name,
        customerName = sale.customerName,
        photo = sale.photo,
        address = sale.address,
        phoneNumber = sale.phoneNumber,
        quantity = sale.quantity,
        purchasePrice = sale.purchasePrice,
        salePrice = sale.salePrice,
        deliveryPrice = sale.deliveryPrice,
        categories = sale.categories,
        company = sale.company,
        amazonCode = sale.amazonCode,
        amazonRequestNumber = sale.amazonRequestNumber,
        amazonTax = sale.amazonTax,
        amazonProfit = sale.amazonProfit,
        amazonSKU = sale.amazonSKU,
        resaleProfit = sale.resaleProfit,
        totalProfit = sale.totalProfit,
        dateOfSale = sale.dateOfSale,
        dateOfPayment = sale.dateOfPayment,
        shippingDate = sale.shippingDate,
        deliveryDate = sale.deliveryDate,
        isOrderedByCustomer = sale.isOrderedByCustomer,
        isPaidByCustomer = sale.isPaidByCustomer,
        delivered = sale.delivered,
        canceled = sale.canceled,
        isAmazon = sale.isAmazon,
        timestamp = sale.timestamp
    )
}