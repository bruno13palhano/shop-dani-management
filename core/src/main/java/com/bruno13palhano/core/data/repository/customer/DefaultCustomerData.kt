package com.bruno13palhano.core.data.repository.customer

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CustomerTableQueries
import cache.VersionTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultCustomerData
    @Inject
    constructor(
        private val customerQueries: CustomerTableQueries,
        private val versionQueries: VersionTableQueries,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    ) : CustomerData {
        override suspend fun insert(
            model: Customer,
            version: DataVersion,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ): Long {
            var id = 0L

            try {
                if (model.isNew()) {
                    customerQueries.transaction {
                        customerQueries.insert(
                            name = model.name,
                            photo = model.photo,
                            email = model.email,
                            address = model.address,
                            city = model.city,
                            phoneNumber = model.phoneNumber,
                            gender = model.gender,
                            age = model.age.toLong(),
                            timestamp = model.timestamp,
                        )
                        id = customerQueries.getLastId().executeAsOne()

                        versionQueries.insertWithId(
                            id = version.id,
                            name = version.name,
                            timestamp = version.timestamp,
                        )

                        onSuccess(id)
                    }
                } else {
                    customerQueries.transaction {
                        customerQueries.insertWithId(
                            id = model.id,
                            name = model.name,
                            photo = model.photo,
                            email = model.email,
                            address = model.address,
                            city = model.city,
                            phoneNumber = model.phoneNumber,
                            gender = model.gender,
                            age = model.age.toLong(),
                            timestamp = model.timestamp,
                        )

                        versionQueries.insertWithId(
                            id = version.id,
                            name = version.name,
                            timestamp = version.timestamp,
                        )

                        id = model.id
                        onSuccess(model.id)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(1)
            }

            return id
        }

        override suspend fun update(
            model: Customer,
            version: DataVersion,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            try {
                customerQueries.transaction {
                    customerQueries.update(
                        id = model.id,
                        name = model.name,
                        photo = model.photo,
                        email = model.email,
                        address = model.address,
                        city = model.city,
                        phoneNumber = model.phoneNumber,
                        gender = model.gender,
                        age = model.age.toLong(),
                        timestamp = model.timestamp,
                    )

                    versionQueries.update(
                        name = version.name,
                        timestamp = version.timestamp,
                        id = version.id,
                    )

                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(2)
            }
        }

        override suspend fun deleteById(
            id: Long,
            version: DataVersion,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            try {
                customerQueries.transaction {
                    customerQueries.delete(id)

                    versionQueries.update(
                        name = version.name,
                        timestamp = version.timestamp,
                        id = version.id,
                    )

                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(3)
            }
        }

        override fun getAll(): Flow<List<Customer>> {
            return customerQueries.getAll(::mapCustomer)
                .asFlow().mapToList(ioDispatcher)
        }

        override fun search(search: String): Flow<List<Customer>> {
            return customerQueries.search(
                name = search,
                email = search,
                address = search,
                phoneNumber = search,
                mapper = ::mapCustomer,
            ).asFlow().mapToList(ioDispatcher)
        }

        override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Customer>> {
            return if (isOrderedAsc) {
                customerQueries.getByNameAsc(mapper = ::mapCustomer)
                    .asFlow().mapToList(ioDispatcher)
            } else {
                customerQueries.getByNameDesc(mapper = ::mapCustomer)
                    .asFlow().mapToList(ioDispatcher)
            }
        }

        override fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<Customer>> {
            return if (isOrderedAsc) {
                customerQueries.getByAddressAsc(mapper = ::mapCustomer)
                    .asFlow().mapToList(ioDispatcher)
            } else {
                customerQueries.getByAddressDesc(mapper = ::mapCustomer)
                    .asFlow().mapToList(ioDispatcher)
            }
        }

        override fun getById(id: Long): Flow<Customer> {
            return customerQueries.getById(id, mapper = ::mapCustomer)
                .asFlow().mapToOne(ioDispatcher)
                .catch { it.printStackTrace() }
        }

        override fun getLast(): Flow<Customer> {
            return customerQueries.getLast(mapper = ::mapCustomer)
                .asFlow().mapToOne(ioDispatcher)
                .catch { it.printStackTrace() }
        }

        private fun mapCustomer(
            id: Long,
            name: String,
            photo: ByteArray,
            email: String,
            address: String,
            city: String,
            phoneNumber: String,
            gender: String,
            age: Long,
            timestamp: String,
        ) = Customer(
            id = id,
            name = name,
            photo = photo,
            email = email,
            address = address,
            city = city,
            phoneNumber = phoneNumber,
            gender = gender,
            age = age.toInt(),
            timestamp = timestamp,
        )
    }
