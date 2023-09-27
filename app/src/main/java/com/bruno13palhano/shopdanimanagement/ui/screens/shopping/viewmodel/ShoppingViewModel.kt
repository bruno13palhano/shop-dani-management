package com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    val shoppingList = stockRepository.getItems(isOrderedByCustomer = false)
        .map {
            it.map { shopping ->
                CommonItem(
                    id = shopping.id,
                    photo = shopping.photo,
                    title = shopping.name,
                    subtitle = shopping.quantity.toString(),
                    description = dateFormat.format(shopping.date)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}