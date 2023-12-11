package com.bruno13palhano.core.data.repository.customer

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CustomerTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultCustomerData @Inject constructor(
    private val customerQueries: CustomerTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CustomerData {
    override suspend fun insert(
        model: Customer,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        try {
            if (model.isNew()) {
                customerQueries.insert(
                    name = model.name,
                    photo = model.photo,
                    email = model.email,
                    address = model.address,
                    phoneNumber = model.phoneNumber,
                    timestamp = model.timestamp
                )
                val id = customerQueries.getLastId().executeAsOne()
                onSuccess(id)

                return id
            } else {
                customerQueries.insertWithId(
                    id = model.id,
                    name = model.name,
                    photo = model.photo,
                    email = model.email,
                    address = model.address,
                    phoneNumber = model.phoneNumber,
                    timestamp = model.timestamp
                )
                onSuccess(model.id)

                return model.id
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(1)

            return 0L
        }
    }

    override suspend fun update(
        model: Customer,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            customerQueries.update(
                id = model.id,
                name = model.name,
                photo = model.photo,
                email = model.email,
                address = model.address,
                phoneNumber = model.phoneNumber,
                timestamp = model.timestamp
            )
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(2)
        }
    }

    override suspend fun deleteById(
        id: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            customerQueries.delete(id)
            onSuccess()
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
            mapper = ::mapCustomer
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
        phoneNumber: String,
        timestamp: String
    ) = Customer(
        id = id,
        name = name,
        photo = photo,
        email = email,
        address = address,
        phoneNumber = phoneNumber,
        timestamp = timestamp
    )
}