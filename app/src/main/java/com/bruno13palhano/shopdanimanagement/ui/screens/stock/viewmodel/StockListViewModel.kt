package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.DefaultStockOrderRepository
import com.bruno13palhano.core.model.StockOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    @DefaultStockOrderRepository stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    val stockList = stockRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}