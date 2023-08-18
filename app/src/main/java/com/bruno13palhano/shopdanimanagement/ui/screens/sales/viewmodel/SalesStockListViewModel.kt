package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.DefaultStockOrderRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.core.model.StockOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SalesStockListViewModel @Inject constructor(
    @DefaultStockOrderRepository private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    val stockList = stockRepository.getAll()
        .map {
            it.map { stock ->
                Stock(
                    id = stock.id,
                    name = stock.name,
                    photo = stock.photo,
                    purchasePrice = stock.purchasePrice,
                    quantity = stock.quantity
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}