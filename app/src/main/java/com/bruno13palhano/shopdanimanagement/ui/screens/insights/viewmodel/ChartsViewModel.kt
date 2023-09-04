package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.Shopping
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>,
    @DefaultShoppingRepository private val shoppingRepository: ShoppingData<Shopping>,
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

    private val _lastSalesEntry = MutableStateFlow(ChartEntryModelProducer())
    val lastSalesEntry = _lastSalesEntry
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    private val _stockSales = saleRepository.getAllStockSales(0, 100)
    private val _shopping = shoppingRepository.getItemsLimited(0, 100)
    private val _chartEntry = MutableStateFlow(ChartEntryModelProducer())
    val chartEntry = _chartEntry
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    private val _allSales = MutableStateFlow(ChartEntryModelProducer() + ChartEntryModelProducer())
    val allSales = _allSales
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer() + ChartEntryModelProducer()
        )

    fun setItemsDaysRange(rangeOfDays: Int) {
        val stockChart = mutableListOf<Pair<String, Float>>()
        val ordersChart = mutableListOf<Pair<String, Float>>()

        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            saleRepository.getLastSales(0, 100)
                .map {
                    it.filter { sale -> !sale.isOrderedByCustomer }
                        .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(stockChart, days)

                    it.filter { sale -> sale.isOrderedByCustomer }
                        .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
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

        viewModelScope.launch {
            combine(_stockSales, _shopping) { stockSales, shopping ->
                val stockSalesChart = mutableListOf<Pair<String, Float>>()
                val shoppingChart = mutableListOf<Pair<String, Float>>()

                days = Array(rangeOfDays) { 0 }
                stockSales.map { setQuantity(days, it.dateOfSale, it.quantity) }
                setChartEntries(stockSalesChart, days)

                days = Array(rangeOfDays) { 0 }
                shopping.map { setQuantity(days, it.date, it.quantity) }
                setChartEntries(shoppingChart, days)

                ChartEntryModelProducer(
                    stockSalesChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y)
                    },
                    shoppingChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y)
                    }
                )
            }
                .collect {
                    _chartEntry.value = it
                }
        }

        val chart = mutableListOf<Pair<String, Float>>()
        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            saleRepository.getLastSales(0, 100).collect {
                it.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(chart, days)

                _lastSalesEntry.value = ChartEntryModelProducer(
                    chart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y)
                    }
                )
            }
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
}