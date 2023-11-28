package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class CustomerNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : CustomerNetwork {
    override suspend fun getAll(): List<Customer> {
        return try {
            apiService.getAllCustomers().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteCustomer(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: Customer) {
        try {
            apiService.updateCustomer(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun insert(data: Customer) {
        try {
            apiService.insertCustomer(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}