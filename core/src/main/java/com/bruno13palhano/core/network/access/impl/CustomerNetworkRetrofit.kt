package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.model.CustomerNet
import javax.inject.Inject

internal class CustomerNetworkRetrofit
    @Inject
    constructor(
        private val apiService: Service,
    ) : CustomerNetwork {
        override suspend fun getAll(): List<CustomerNet> {
            return try {
                apiService.getAllCustomers()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                emptyList()
            }
        }

        override suspend fun delete(id: Long) = apiService.deleteCustomer(id)

        override suspend fun update(data: CustomerNet) = apiService.updateCustomer(data)

        override suspend fun insert(data: CustomerNet) = apiService.insertCustomer(data)
    }
