package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCustomerLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.di.DefaultCustomerNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class CustomerRepository @Inject constructor(
    @DefaultCustomerNet private val customerNet: CustomerNetwork,
    @InternalCustomerLight private val customerData: CustomerData<Customer>,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CustomerData<Customer> {
    override suspend fun insert(model: Customer): Long {
        CoroutineScope(ioDispatcher).launch {
            customerNet.insert(model)
        }
        return customerData.insert(model = model)
    }

    override suspend fun update(model: Customer) {
        customerData.update(model = model)
    }

    override suspend fun deleteById(id: Long) {
        customerData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Customer>> {
        CoroutineScope(ioDispatcher).launch {
            customerNet.getAll().forEach { customerData.insert(it) }
        }
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
}