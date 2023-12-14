package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCustomerLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultCustomerNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultCustomerRepository @Inject constructor(
    @DefaultCustomerNet private val customerNetwork: CustomerNetwork,
    @InternalCustomerLight private val customerData: CustomerData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CustomerRepository {
    override suspend fun insert(
        model: Customer,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val customerVersion = Versions.customerVersion(timestamp = model.timestamp)

        val id = customerData.insert(model = model, version = customerVersion, onError = onError) {
            val netModel = Customer(
                id = it,
                name = model.name,
                photo = model.photo,
                email = model.email,
                address = model.address,
                phoneNumber = model.phoneNumber,
                timestamp = model.timestamp
            )

            CoroutineScope(ioDispatcher).launch {
                try {
                    customerNetwork.insert(data = netModel)
                    versionNetwork.insert(data = customerVersion)
                    onSuccess(netModel.id)
                }
                catch (e: Exception) { onError(Errors.INSERT_SERVER_ERROR) }
            }
        }

        return id
    }

    override suspend fun update(
        model: Customer,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val customerVersion = Versions.customerVersion(timestamp = model.timestamp)

        customerData.update(model = model, version = customerVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    customerNetwork.update(data = model)
                    versionNetwork.update(data = customerVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(Errors.UPDATE_SERVER_ERROR) }
            }
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val customerVersion = Versions.customerVersion(timestamp = timestamp)

        customerData.deleteById(id = id, version = customerVersion, onError = onError) {
            CoroutineScope(ioDispatcher).launch {
                try {
                    customerNetwork.delete(id = id)
                    versionNetwork.update(data = customerVersion)
                    onSuccess()
                }
                catch (e: Exception) { onError(Errors.DELETE_SERVER_ERROR) }
            }
        }
    }

    override fun getAll(): Flow<List<Customer>> {
        return customerData.getAll()
    }

    override fun search(search: String): Flow<List<Customer>> {
        return customerData.search(search = search)
    }

    override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Customer>> {
        return customerData.getOrderedByName(isOrderedAsc = isOrderedAsc)
    }

    override fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<Customer>> {
        return customerData.getOrderedByAddress(isOrderedAsc = isOrderedAsc)
    }

    override fun getById(id: Long): Flow<Customer> {
        return customerData.getById(id = id)
    }

    override fun getLast(): Flow<Customer> {
        return customerData.getLast()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.syncData(
            dataVersion = getDataVersion(versionData, Versions.customerVersionId),
            networkVersion = getNetworkVersion(versionNetwork, Versions.customerVersionId),
            dataList = getDataList(customerData),
            networkList = getNetworkList(customerNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { customerNetwork.delete(it) }
                saveList.forEach { customerNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { customerData.deleteById(it, netVersion, {}) {} }
                saveList.forEach { customerData.insert(it, netVersion, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )
}