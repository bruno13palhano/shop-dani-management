package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.data.di.SecondarySaleRepository
import com.bruno13palhano.core.data.di.SecondaryShoppingRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.Shopping
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ShoppingVsSalesViewModel @Inject constructor(
    @SecondarySaleRepository private val saleRepository: SaleData<Sale>,
    @SecondaryShoppingRepository private val shoppingRepository: ShoppingData<Shopping>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

    private val _stockSales = saleRepository.getAllStockSales(0, 100)
    private val _shopping = shoppingRepository.getItemsLimited(0, 100)

    private val _chartEntry = MutableStateFlow(ChartEntryModelProducer())
    val chartEntry = _chartEntry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    fun getChartByRange(rangeOfDays: Int) {
        viewModelScope.launch {
            combine(_stockSales, _shopping) { stockSales, shopping ->
                val stockChart = mutableListOf<Pair<String, Float>>()
                val shoppingChart = mutableListOf<Pair<String, Float>>()

                days = Array(rangeOfDays) { 0 }
                stockSales.map { setDay(days, it.dateOfSale, it.quantity) }
                setChartEntries(stockChart, days)

                days = Array(rangeOfDays) { 0 }
                shopping.map { setDay(days, it.date, it.quantity) }
                setChartEntries(shoppingChart, days)

                ChartEntryModelProducer(
                    stockChart.mapIndexed { index, (date, y) ->
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
    }

    private fun setDay(days: Array<Int>, date: Long, quantity: Int) {
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