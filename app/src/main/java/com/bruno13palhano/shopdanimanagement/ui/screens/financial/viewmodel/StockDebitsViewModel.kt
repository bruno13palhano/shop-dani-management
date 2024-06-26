package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.StockRep
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDebitsViewModel
    @Inject
    constructor(
        @StockRep private val stockRepository: StockRepository
    ) : ViewModel() {
        private val _debitItems = MutableStateFlow(emptyList<StockItem>())
        val debitItems =
            _debitItems
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

        fun getDebitStock() {
            viewModelScope.launch {
                stockRepository.getDebitStock().collect {
                    _debitItems.value = it
                }
            }
        }

        fun getStockByPrice(isOrderedAsc: Boolean) {
            viewModelScope.launch {
                stockRepository.getDebitStockByPrice(isOrderedAsc = isOrderedAsc).collect {
                    _debitItems.value = it
                }
            }
        }

        fun getStockByName(isOrderedAsc: Boolean) {
            viewModelScope.launch {
                stockRepository.getDebitStockByName(isOrderedAsc = isOrderedAsc).collect {
                    _debitItems.value = it
                }
            }
        }
    }