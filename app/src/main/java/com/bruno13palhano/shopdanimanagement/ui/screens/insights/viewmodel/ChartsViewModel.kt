package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.shopdanimanagement.ui.screens.setChartEntries
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var days = arrayOf(0)

    private val _lastSalesEntries = MutableStateFlow(SalesEntries())
    val lastSalesEntries = _lastSalesEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SalesEntries()
        )

    fun setItemsDaysRange(rangeOfDays: Int) {
        val stockEntries = mutableListOf<Pair<String, Float>>()
        val ordersEntries = mutableListOf<Pair<String, Float>>()
        val lastSalesEntries = mutableListOf<Pair<String, Float>>()

        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            saleRepository.getLastSales(0, 100).map {
                it.filter { sale -> !sale.isOrderedByCustomer }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(stockEntries, days)

                days = Array(rangeOfDays) { 0 }

                it.filter { sale -> sale.isOrderedByCustomer }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(ordersEntries, days)

                days = Array(rangeOfDays) { 0 }

                it.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(lastSalesEntries, days)

                SalesEntries(
                    lastSalesEntries = lastSalesEntries,
                    stockEntries = stockEntries,
                    orderEntries = ordersEntries
                )
            }
                .collect { _lastSalesEntries.value = it }
        }
    }

    data class SalesEntries(
        val lastSalesEntries: List<Pair<String, Float>> = listOf(),
        val stockEntries: List<Pair<String, Float>> = listOf(),
        val orderEntries: List<Pair<String, Float>> = listOf()
    )
}