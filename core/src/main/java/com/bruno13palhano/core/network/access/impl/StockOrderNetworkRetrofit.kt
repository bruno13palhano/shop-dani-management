package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.StockOrderNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class StockOrderNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : StockOrderNetwork {
    override suspend fun getAll(): List<StockOrder> {
        return try {
            apiService.getAllItems().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) = apiService.deleteItem(id)

    override suspend fun update(data: StockOrder) =
        apiService.updateItem(data.asNetwork())

    override suspend fun insert(data: StockOrder) =
        apiService.insertItem(data.asNetwork())
}