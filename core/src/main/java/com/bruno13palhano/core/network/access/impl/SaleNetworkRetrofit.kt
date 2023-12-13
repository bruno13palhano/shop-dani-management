package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class SaleNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : SaleNetwork {
    override suspend fun getAll(): List<Sale> {
        return try {
            apiService.getAllSales().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) = apiService.deleteSale(id)

    override suspend fun update(data: Sale) =
        apiService.updateSale(data.asNetwork())

    override suspend fun insert(data: Sale) {
        apiService.insertSale(data.asNetwork())
    }
}