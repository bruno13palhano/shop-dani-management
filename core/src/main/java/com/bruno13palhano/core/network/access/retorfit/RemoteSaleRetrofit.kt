package com.bruno13palhano.core.network.access.retorfit

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.RemoteSaleData
import com.bruno13palhano.core.network.model.SaleNet
import javax.inject.Inject

internal class RemoteSaleRetrofit
    @Inject
    constructor(
        private val apiService: Service
    ) : RemoteSaleData {
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