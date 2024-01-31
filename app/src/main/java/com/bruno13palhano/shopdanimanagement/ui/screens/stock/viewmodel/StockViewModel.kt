package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.StockRep
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    @StockRep private val stockRepository: StockRepository,
    @CategoryRep private val categoryRepository: CategoryRepository
) : ViewModel() {
    val categories = categoryRepository.getAll()
        .map {
            it.map { category -> category.category}
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private var _stockList = MutableStateFlow(emptyList<Stock>())
    val stockList = _stockList
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getItems() {
        viewModelScope.launch {
            stockRepository.getStockItems().collect {
                _stockList.value = it.map { stockItem ->
                    Stock(
                        id = stockItem.id,
                        name = stockItem.name,
                        photo = stockItem.photo,
                        purchasePrice = stockItem.purchasePrice,
                        quantity = stockItem.quantity
                    )
                }
            }
        }
    }

    fun getItemsByCategories(category: String) {
        viewModelScope.launch {
            stockRepository.getByCategory(category).collect {
                _stockList.value = it.map { stockItem ->
                    Stock(
                        id = stockItem.id,
                        name = stockItem.name,
                        photo = stockItem.photo,
                        purchasePrice = stockItem.purchasePrice,
                        quantity = stockItem.quantity
                    )
                }
            }
        }
    }

    fun getOutOfStock() {
        viewModelScope.launch {
            stockRepository.getOutOfStock().collect {
                _stockList.value = it.map { stockItem ->
                    Stock(
                        id = stockItem.id,
                        name = stockItem.name,
                        photo = stockItem.photo,
                        purchasePrice = stockItem.purchasePrice,
                        quantity = stockItem.quantity
                    )
                }
            }
        }
    }

    fun getItemsByCode(code: String) {
        viewModelScope.launch {
            stockRepository.getByCode(code = code).collect {
                _stockList.value = it.map { stockItem ->
                    Stock(
                        id = stockItem.id,
                        name = stockItem.name,
                        photo = stockItem.photo,
                        purchasePrice = stockItem.purchasePrice,
                        quantity = stockItem.quantity
                    )
                }
            }
        }
    }
}