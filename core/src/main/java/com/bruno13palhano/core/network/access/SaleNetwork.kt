package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder

interface SaleNetwork : CrudNetwork<Sale> {
    suspend fun insertItems(sale: Sale, stockOrder: StockOrder, delivery: Delivery)
}