package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
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
class CompanySalesViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

    private val _chartEntry = MutableStateFlow(ChartEntryModelProducer())
    val chartEntry = _chartEntry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    fun getChartByRange(rangeOfDays: Int) {
        viewModelScope.launch {
            saleRepository.getAll().collect {
                val avonChart = mutableListOf<Pair<String, Float>>()
                val naturaChart = mutableListOf<Pair<String, Float>>()

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.AVON.company }
                    .map { sale -> setDay(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(avonChart, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.NATURA.company }
                    .map { sale -> setDay(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(naturaChart, days)

                _chartEntry.value = ChartEntryModelProducer(
                    avonChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y)
                    },
                    naturaChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y)
                    }
                )
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