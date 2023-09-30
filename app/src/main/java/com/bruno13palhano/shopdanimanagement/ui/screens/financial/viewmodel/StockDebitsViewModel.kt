package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StockDebitsViewModel @Inject constructor(
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    val debitItems = stockRepository.getDebitStock()
        .map {
            it.map { stockItem ->
                Stock(
                    id = stockItem.id,
                    name = stockItem.name,
                    photo = stockItem.photo,
                    purchasePrice = stockItem.purchasePrice,
                    quantity = stockItem.quantity
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}