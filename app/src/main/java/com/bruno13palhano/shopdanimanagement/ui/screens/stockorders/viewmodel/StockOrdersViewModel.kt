package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.core.model.StockOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockOrdersViewModel @Inject constructor(
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>,
    @CategoryRep private val categoryRepository: CategoryData<Category>
) : ViewModel() {
    val categories = categoryRepository.getAll()
        .map {
            it.map { category -> category.name}
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

    fun getItems(isOrderedByCustomer: Boolean) {
        viewModelScope.launch {
            stockRepository.getItems(isOrderedByCustomer).collect {
                _stockList.value = it.map { stockOrder ->
                    Stock(
                        id = stockOrder.id,
                        name = stockOrder.name,
                        photo = stockOrder.photo,
                        purchasePrice = stockOrder.purchasePrice,
                        quantity = stockOrder.quantity
                    )
                }
            }
        }
    }

    fun getItemsByCategories(category: String, isOrderedByCustomer: Boolean) {
        viewModelScope.launch {
            stockRepository.getByCategory(category, isOrderedByCustomer).collect {
                _stockList.value = it.map { stockOrder ->
                    Stock(
                        id = stockOrder.id,
                        name = stockOrder.name,
                        photo = stockOrder.photo,
                        purchasePrice = stockOrder.purchasePrice,
                        quantity = stockOrder.quantity
                    )
                }
            }
        }
    }
}