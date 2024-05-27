package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.network.model.StockItemNet

interface StockNetwork : CrudNetwork<StockItemNet> {
    suspend fun updateItemQuantity(id: Long, quantity: Int)
}