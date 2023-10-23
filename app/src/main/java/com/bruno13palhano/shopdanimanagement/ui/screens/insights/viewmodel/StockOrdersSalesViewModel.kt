package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Sale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
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
class StockOrdersSalesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

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
                        .map { sale -> setQuantityOfSalesInTheDay(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(stockEntries, days)

                    it.filter { sale -> sale.isOrderedByCustomer }
                        .map { sale -> setQuantityOfSalesInTheDay(days, sale.dateOfSale, sale.quantity) }
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

    private fun setQuantityOfSalesInTheDay(days: Array<Int>, date: Long, quantity: Int) {
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

    data class ItemsSalesEntries(
        val stockEntries: List<Pair<String, Float>> = listOf(),
        val ordersEntries: List<Pair<String, Float>> = listOf()
    )
}