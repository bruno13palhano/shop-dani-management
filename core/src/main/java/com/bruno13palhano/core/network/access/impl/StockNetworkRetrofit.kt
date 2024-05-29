package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.StockNetwork
import com.bruno13palhano.core.network.model.StockItemNet
import javax.inject.Inject

internal class StockNetworkRetrofit
    @Inject
    constructor(
        private val apiService: Service,
    ) : StockNetwork {
        override suspend fun updateItemQuantity(
            id: Long,
            quantity: Int,
        ) = apiService.updateStockItemQuantity(id = id, quantity = quantity)

        override suspend fun getAll(): List<StockItemNet> {
            return try {
                apiService.getAllItems()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                emptyList()
            }
        }

        override suspend fun delete(id: Long) = apiService.deleteItem(id)

        override suspend fun update(data: StockItemNet) = apiService.updateItem(data)

        override suspend fun insert(data: StockItemNet) = apiService.insertItem(data)
    }
