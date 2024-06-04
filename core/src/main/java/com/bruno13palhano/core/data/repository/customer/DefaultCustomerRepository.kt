package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCustomer
import com.bruno13palhano.core.data.di.InternalVersion
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.customerNetToCustomer
import com.bruno13palhano.core.data.repository.customerToCustomerNet
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.data.repository.versionToVersionNet
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.network.access.RemoteCustomerData
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.di.FirebaseCustomer
import com.bruno13palhano.core.network.di.FirebaseVersion
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultCustomerRepository
    @Inject
    constructor(
        @FirebaseCustomer private val remoteCustomerData: RemoteCustomerData,
        @InternalCustomer private val customerData: CustomerData,
        @InternalVersion private val versionData: VersionData,
        @FirebaseVersion private val remoteVersionData: RemoteVersionData,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    ) : CustomerRepository {
        override suspend fun insert(
            model: Customer,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ): Long {
            val customerVersion = Versions.customerVersion(timestamp = model.timestamp)

            val id =
                customerData.insert(model = model, version = customerVersion, onError = onError) {
                    val netModel =
                        customerToCustomerNet(
                            Customer(
                                id = it,
                                name = model.name,
                                photo = model.photo,
                                email = model.email,
                                address = model.address,
                                city = model.city,
                                phoneNumber = model.phoneNumber,
                                gender = model.gender,
                                age = model.age,
                                timestamp = model.timestamp,
                            ),
                        )

                    CoroutineScope(ioDispatcher).launch {
                        try {
                            remoteCustomerData.insert(data = netModel)
                            remoteVersionData.insert(data = versionToVersionNet(customerVersion))
                            onSuccess(netModel.id)
                        } catch (e: Exception) {
                            onError(Errors.INSERT_SERVER_ERROR)
                        }
                    }
                }

            return id
        }

        override suspend fun update(
            model: Customer,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val customerVersion = Versions.customerVersion(timestamp = model.timestamp)

            customerData.update(model = model, version = customerVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteCustomerData.update(data = customerToCustomerNet(model))
                        remoteVersionData.update(data = versionToVersionNet(customerVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.UPDATE_SERVER_ERROR)
                    }
                }
            }
        }

        override suspend fun deleteById(
            id: Long,
            timestamp: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val customerVersion = Versions.customerVersion(timestamp = timestamp)

            customerData.deleteById(id = id, version = customerVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteCustomerData.delete(id = id)
                        remoteVersionData.update(data = versionToVersionNet(customerVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.DELETE_SERVER_ERROR)
                    }
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
                dataVersion = getDataVersion(versionData, Versions.CUSTOMER_VERSION_ID),
                networkVersion = getNetworkVersion(remoteVersionData, Versions.CUSTOMER_VERSION_ID),
                dataList = getDataList(customerData),
                networkList =
                    getNetworkList(remoteCustomerData).map {
                        println(it)
                        customerNetToCustomer(it)
                    },
                onPush = { deleteIds, saveList, dtVersion ->
                    deleteIds.forEach { remoteCustomerData.delete(it) }
                    saveList.forEach { remoteCustomerData.insert(customerToCustomerNet(it)) }
                    remoteVersionData.insert(versionToVersionNet(dtVersion))
                },
                onPull = { deleteIds, saveList, netVersion ->
                    deleteIds.forEach { customerData.deleteById(it, netVersion, {}) {} }
                    saveList.forEach { customerData.insert(it, netVersion, {}) {} }
                    versionData.insert(netVersion, {}) {}
                },
            )
    }
