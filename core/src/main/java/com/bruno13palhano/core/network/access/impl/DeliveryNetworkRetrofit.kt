package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.DeliveryNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class DeliveryNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : DeliveryNetwork {
    override suspend fun getAll(): List<Delivery> {
        return try {
            apiService.getAllDeliveries().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteDelivery(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: Delivery) {
        try {
            apiService.updateDelivery(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun insert(data: Delivery) {

    }
}