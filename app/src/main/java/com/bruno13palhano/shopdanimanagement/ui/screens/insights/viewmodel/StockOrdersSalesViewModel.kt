package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
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
import javax.inject.Inject

@HiltViewModel
class StockOrdersSalesViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

    private val _allSales = MutableStateFlow(ChartEntryModelProducer() + ChartEntryModelProducer())
    val allSales = _allSales
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer() + ChartEntryModelProducer()
        )

    fun setStockOrdersSalesRange(rangeOfDays: Int) {
        val stockChart = mutableListOf<Pair<String, Float>>()
        val ordersChart = mutableListOf<Pair<String, Float>>()

        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            saleRepository.getLastSales(0, 100)
                .map {
                    it.filter { sale -> !sale.isOrderedByCustomer }
                        .map { sale -> setQuantityOfSalesInTheDay(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(stockChart, days)

                    it.filter { sale -> sale.isOrderedByCustomer }
                        .map { sale -> setQuantityOfSalesInTheDay(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(ordersChart, days)

                    ChartEntryModelProducer(stockChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y) }
                    ) +
                    ChartEntryModelProducer(ordersChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y) }
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
            chart.add(Pair(currentDay.minusDays(i.toLong()).dayOfWeek.name, days[i].toFloat()))
        }
    }
}