package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.model.SaleItemsNet
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

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteSale(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: Sale) {
        try {
            apiService.updateSale(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun insert(data: Sale) {

    }

    override suspend fun insertItems(sale: Sale, stockOrder: StockOrder, delivery: Delivery) {
        try {
            val saleItemsNet = SaleItemsNet(sale.asNetwork(), stockOrder.asNetwork(), delivery.asNetwork())

            apiService.insertSale(saleItemsNet)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}