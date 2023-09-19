package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.di.InternalCustomerLight
import com.bruno13palhano.core.model.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    @InternalCustomerLight private val customerData: InternalCustomerData
) : CustomerData<Customer> {
    override suspend fun insert(model: Customer): Long {
        return customerData.insert(model = model)
    }

    override suspend fun update(model: Customer) {
        customerData.update(model = model)
    }

    override suspend fun delete(model: Customer) {
        customerData.delete(model = model)
    }

    override suspend fun deleteById(id: Long) {
        customerData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Customer>> {
        return customerData.getAll()
    }

    override fun getById(id: Long): Flow<Customer> {
        return customerData.getById(id = id)
    }

    override fun getLast(): Flow<Customer> {
        return customerData.getLast()
    }
}