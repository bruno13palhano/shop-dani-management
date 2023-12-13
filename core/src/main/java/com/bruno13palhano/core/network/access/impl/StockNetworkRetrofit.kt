package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.StockNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class StockNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : StockNetwork {
    override suspend fun updateItemQuantity(id: Long, quantity: Int) =
        apiService.updateStockItemQuantity(id = id, quantity = quantity)

    override suspend fun getAll(): List<StockItem> {
        return try {
            apiService.getAllItems().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) = apiService.deleteItem(id)

    override suspend fun update(data: StockItem) = apiService.updateItem(data.asNetwork())

    override suspend fun insert(data: StockItem) = apiService.insertItem(data.asNetwork())
}