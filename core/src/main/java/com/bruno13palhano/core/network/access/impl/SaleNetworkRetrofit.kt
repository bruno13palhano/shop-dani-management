package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.model.SaleNet
import javax.inject.Inject

internal class SaleNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : SaleNetwork {
    override suspend fun getAll(): List<SaleNet> {
        return try {
            apiService.getAllSales()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) = apiService.deleteSale(id)

    override suspend fun update(data: SaleNet) = apiService.updateSale(data)

    override suspend fun insert(data: SaleNet) = apiService.insertSale(data)
}