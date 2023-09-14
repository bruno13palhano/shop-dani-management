package com.bruno13palhano.core.data.repository

import cache.CustomerTableQueries
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.model.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerCache @Inject constructor(
    private val customerCache: CustomerTableQueries
) : CustomerData<Customer> {
    override suspend fun insert(model: Customer): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(model: Customer) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(model: Customer) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Customer>> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Flow<Customer> {
        TODO("Not yet implemented")
    }

    override fun getLast(): Flow<Customer> {
        TODO("Not yet implemented")
    }
}