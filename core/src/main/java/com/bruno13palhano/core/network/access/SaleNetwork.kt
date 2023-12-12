package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockItem

interface SaleNetwork : CrudNetwork<Sale> {
    suspend fun insertItems(sale: Sale, stockItem: StockItem, delivery: Delivery)
}