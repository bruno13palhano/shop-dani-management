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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LastSalesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var days = arrayOf(0)

    private val _lastSalesEntry = MutableStateFlow(listOf<Pair<String, Float>>())
    val lastSalesEntry = _lastSalesEntry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun setLastSalesEntryByRange(rangeOfDays: Int) {
        val chart = mutableListOf<Pair<String, Float>>()
        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            saleRepository.getLastSales(0, 100).collect {
                it.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(chart, days)

                _lastSalesEntry.value = chart
            }
        }
    }
}