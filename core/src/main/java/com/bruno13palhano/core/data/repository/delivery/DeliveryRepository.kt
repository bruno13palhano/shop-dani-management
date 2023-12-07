package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.network.access.DeliveryNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultDeliveryNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

internal class DeliveryRepository @Inject constructor(
    @DefaultDeliveryNet private val deliveryNetwork: DeliveryNetwork,
    @InternalDeliveryLight private val deliveryData: DeliveryData<Delivery>,
    @InternalVersionLight private val versionLight: VersionData<DataVersion>,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
) : DeliveryData<Delivery> {
    override suspend fun insert(model: Delivery): Long {
        versionLight.insert(DataVersion(5L, "DELIVERY", model.timestamp))
        return deliveryData.insert(model = model)
    }

    override suspend fun update(model: Delivery) {
        versionLight.update(DataVersion(5L, "DELIVERY", model.timestamp))
        deliveryData.update(model = model)
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

    override suspend fun deleteById(id: Long) {
        versionLight.update(DataVersion(5L, "DELIVERY", OffsetDateTime.now()))
        deliveryData.deleteById(id = id)
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
            dataVersion = getDataVersion(versionLight, 5L),
            networkVersion = getNetworkVersion(versionNetwork, 5L),
            dataList = getDataList(deliveryData),
            networkList = getNetworkList(deliveryNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { deliveryNetwork.delete(it) }
                saveList.forEach { deliveryNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { deliveryData.deleteById(it) }
                saveList.forEach { deliveryData.insert(it) }
                versionLight.insert(netVersion)
            }
        )

    override fun getCanceledDeliveries(): Flow<List<Delivery>> {
        return deliveryData.getCanceledDeliveries()
    }
}