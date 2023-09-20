package com.bruno13palhano.core.data.repository.customer

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CustomerTableQueries
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Customer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CustomerLight @Inject constructor(
    private val customerQueries: CustomerTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CustomerData<Customer> {
    override suspend fun insert(model: Customer): Long {
        customerQueries.insert(
            name = model.name,
            photo = model.photo,
            email = model.email,
            address = model.address,
            phoneNumber = model.phoneNumber
        )
        return customerQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Customer) {
        customerQueries.update(
            id = model.id,
            name = model.name,
            photo = model.photo,
            email = model.email,
            address = model.address,
            phoneNumber = model.phoneNumber
        )
    }

    override suspend fun delete(model: Customer) {
        customerQueries.delete(model.id)
    }

    override suspend fun deleteById(id: Long) {
        customerQueries.delete(id)
    }

    override fun getAll(): Flow<List<Customer>> {
        return customerQueries.getAll(::mapCustomer)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Customer> {
        return customerQueries.getById(id, mapper = ::mapCustomer)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun getLast(): Flow<Customer> {
        return customerQueries.getLast(mapper = ::mapCustomer)
            .asFlow().mapToOne(ioDispatcher)
    }

    private fun mapCustomer(
        id: Long,
        name: String,
        photo: String,
        email: String,
        address: String,
        phoneNumber: String
    ) = Customer(
        id = id,
        name = name,
        photo = photo,
        email = email,
        address = address,
        phoneNumber = phoneNumber
    )
}