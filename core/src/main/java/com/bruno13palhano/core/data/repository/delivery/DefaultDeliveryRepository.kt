package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.network.access.DeliveryNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultDeliveryNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultDeliveryRepository @Inject constructor(
    @DefaultDeliveryNet private val deliveryNetwork: DeliveryNetwork,
    @InternalDeliveryLight private val deliveryData: DeliveryData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DeliveryRepository {
    override suspend fun insert(model: Delivery): Long {
        val deliveryVersion = DataVersion(5L, "DELIVERY", model.timestamp)
        val id = deliveryData.insert(model = model) {
            CoroutineScope(ioDispatcher).launch {
                val netModel = Delivery(
                    id = it,
                    saleId = model.saleId,
                    customerName = model.customerName,
                    address = model.address,
                    phoneNumber = model.phoneNumber,
                    productName = model.productName,
                    price = model.price,
                    deliveryPrice = model.deliveryPrice,
                    shippingDate = model.shippingDate,
                    deliveryDate = model.deliveryDate,
                    delivered = model.delivered,
                    timestamp = model.timestamp
                )

                deliveryNetwork.insert(data = netModel)
            }
        }
        versionData.insert(model = deliveryVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.insert(data = deliveryVersion)
            }
        }

        return id
    }

    override suspend fun update(model: Delivery) {
        val deliveryVersion = DataVersion(5L, "DELIVERY", model.timestamp)
        deliveryData.update(model = model) {
            CoroutineScope(ioDispatcher).launch {
                deliveryNetwork.update(data = model)
            }
        }
        versionData.update(model = deliveryVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.update(data = deliveryVersion)
            }
        }
    }

    override suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float) {
        deliveryData.updateDeliveryPrice(id = id, deliveryPrice = deliveryPrice)
    }

    override suspend fun updateShippingDate(id: Long, shippingDate: Long) {
        deliveryData.updateShippingDate(id = id, shippingDate = shippingDate)
    }

    override suspend fun updateDeliveryDate(id: Long, deliveryDate: Long) {
        deliveryData.updateDeliveryDate(id = id, deliveryDate = deliveryDate)
    }

    override suspend fun updateDelivered(id: Long, delivered: Boolean) {
        deliveryData.updateDelivered(id = id, delivered = delivered)
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Delivery>> {
        return deliveryData.getDeliveries(delivered = delivered)
    }

    override suspend fun deleteById(id: Long, timestamp: String) {
        val deliveryVersion = DataVersion(5L, "DELIVERY", timestamp)
        deliveryData.deleteById(id = id) {
            CoroutineScope(ioDispatcher).launch {
                deliveryNetwork.delete(id = id)
            }
        }
        versionData.update(model = deliveryVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.update(data = deliveryVersion)
            }
        }
    }

    override fun getAll(): Flow<List<Delivery>> {
        return deliveryData.getAll()
    }

    override fun getById(id: Long): Flow<Delivery> {
        return deliveryData.getById(id = id)
    }

    override fun getLast(): Flow<Delivery> {
        return deliveryData.getLast()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.syncData(
            dataVersion = getDataVersion(versionData, 5L),
            networkVersion = getNetworkVersion(versionNetwork, 5L),
            dataList = getDataList(deliveryData),
            networkList = getNetworkList(deliveryNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { deliveryNetwork.delete(it) }
                saveList.forEach { deliveryNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { deliveryData.deleteById(it) {} }
                saveList.forEach { deliveryData.insert(it) {} }
                versionData.insert(netVersion) {}
            }
        )

    override fun getCanceledDeliveries(): Flow<List<Delivery>> {
        return deliveryData.getCanceledDeliveries()
    }
}