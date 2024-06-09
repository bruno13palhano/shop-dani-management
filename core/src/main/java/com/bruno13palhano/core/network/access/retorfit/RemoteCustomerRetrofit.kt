package com.bruno13palhano.core.network.access.retorfit

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.RemoteCustomerData
import com.bruno13palhano.core.network.model.CustomerNet
import javax.inject.Inject

internal class RemoteCustomerRetrofit
    @Inject
    constructor(
        private val apiService: Service
    ) : RemoteCustomerData {
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