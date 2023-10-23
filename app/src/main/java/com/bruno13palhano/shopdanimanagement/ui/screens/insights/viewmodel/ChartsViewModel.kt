package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Sale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

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

    private fun setQuantity(days: Array<Int>, date: Long, quantity: Int) {
        for (i in days.indices) {
            if (LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("UTC")).toLocalDate()
                == currentDay.minusDays(i.toLong())) {
                days[i] += quantity
            }
        }
    }

    private fun setChartEntries(chart: MutableList<Pair<String, Float>>, days: Array<Int>) {
        for (i in days.size-1 downTo 0) {
            chart.add(
                Pair(
                    DateTimeFormatter.ofPattern("dd/MM").format(currentDay.minusDays(i.toLong())),
                    days[i].toFloat()
                )
            )
        }
    }

    data class SalesEntries(
        val lastSalesEntries: List<Pair<String, Float>> = listOf(),
        val stockEntries: List<Pair<String, Float>> = listOf(),
        val orderEntries: List<Pair<String, Float>> = listOf()
    )
}