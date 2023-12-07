package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.shopdanimanagement.ui.screens.setChartEntries
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockOrdersSalesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var days = arrayOf(0)

    private val _allSales = MutableStateFlow(ItemsSalesEntries())
    val allSales = _allSales
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ItemsSalesEntries()
        )

    fun setStockOrdersSalesRange(rangeOfDays: Int) {
        val stockEntries = mutableListOf<Pair<String, Float>>()
        val ordersEntries = mutableListOf<Pair<String, Float>>()

        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            saleRepository.getLastSales(0, 100)
                .map {
                    it.filter { sale -> !sale.isOrderedByCustomer }
                        .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(stockEntries, days)

                    days = Array(rangeOfDays) { 0 }

                    it.filter { sale -> sale.isOrderedByCustomer }
                        .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(ordersEntries, days)

                    ItemsSalesEntries(
                        stockEntries = stockEntries,
                        ordersEntries = ordersEntries
                    )
                }
            .collect {
                _allSales.value = it
            }
        }
    }

    data class ItemsSalesEntries(
        val stockEntries: List<Pair<String, Float>> = listOf(),
        val ordersEntries: List<Pair<String, Float>> = listOf()
    )
}