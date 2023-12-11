package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalDeliveryLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
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
    override suspend fun insert(
        model: Delivery,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val deliveryVersion = Versions.deliveryVersion(timestamp = model.timestamp)

        val id = deliveryData.insert(model = model, onError = onError) {
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

            CoroutineScope(ioDispatcher).launch {
                try { deliveryNetwork.insert(data = netModel) }
                catch (e: Exception) { onError(4) }
            }
        }

        versionData.insert(model = deliveryVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    versionNetwork.insert(data = deliveryVersion)
                    onSuccess(id)
                } catch (e: Exception) { onError(4) }
            }
        }

        return id
    }

    override suspend fun update(
        model: Delivery,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val deliveryVersion = Versions.deliveryVersion(timestamp = model.timestamp)

        deliveryData.update(model = model, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try { deliveryNetwork.update(data = model) }
                catch (e: Exception) { onError(5) }
            }
        }

        versionData.update(model = deliveryVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    versionNetwork.update(data = deliveryVersion)
                    onSuccess()
                } catch (e: Exception) { onError(5) }
            }
        }
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Delivery>> {
        return deliveryData.getDeliveries(delivered = delivered)
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val deliveryVersion = Versions.deliveryVersion(timestamp = timestamp)

        deliveryData.deleteById(id = id, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try { deliveryNetwork.delete(id = id) }
                catch (e: Exception) { onError(6) }
            }
        }

        versionData.update(model = deliveryVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    versionNetwork.update(data = deliveryVersion)
                    onSuccess()
                } catch (e: Exception) { onError(6) }
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
                deleteIds.forEach { deliveryData.deleteById(it, {}) {} }
                saveList.forEach { deliveryData.insert(it, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )

    override fun getCanceledDeliveries(): Flow<List<Delivery>> {
        return deliveryData.getCanceledDeliveries()
    }
}