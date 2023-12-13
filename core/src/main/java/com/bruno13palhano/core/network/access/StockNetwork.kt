package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.StockItem

interface StockNetwork : CrudNetwork<StockItem> {
    suspend fun updateItemQuantity(id: Long, quantity: Int)
}