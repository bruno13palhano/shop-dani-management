package com.bruno13palhano.shopdanimanagement.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val currentDay = LocalDate.now(ZoneId.of("UTC"))

    val lastSales = saleRepository.getLastSales(0, 100)
        .map {
            val days = arrayOf(0,0,0,0,0,0,0)
            val chart = mutableListOf<Pair<String, Float>>()

            it.map { sale -> setDay(days, sale.dateOfSale, sale.quantity) }
            setChartEntries(chart, days)

            ChartEntryModelProducer(
                chart.mapIndexed { index, (date, y) ->
                    DateChartEntry(date, index.toFloat(), y)
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    private fun setDay(days: Array<Int>, date: Long, quantity: Int) {
        when (LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("UTC")).toLocalDate()) {
            currentDay -> {
                days[0] += quantity
            }
            currentDay.minusDays(1) -> {
                days[1] += quantity
            }
            currentDay.minusDays(2) -> {
                days[2] += quantity
            }
            currentDay.minusDays(3) -> {
                days[3] += quantity
            }
            currentDay.minusDays(4) -> {
                days[4] += quantity
            }
            currentDay.minusDays(5) -> {
                days[5] += quantity
            }
            currentDay.minusDays(6) -> {
                days[6] += quantity
            }
            else -> {}
        }
    }

    private fun setChartEntries(chart: MutableList<Pair<String, Float>>, days: Array<Int>) {
        chart.add(Pair(currentDay.minusDays(6).dayOfWeek.name, days[6].toFloat()))
        chart.add(Pair(currentDay.minusDays(5).dayOfWeek.name, days[5].toFloat()))
        chart.add(Pair(currentDay.minusDays(4).dayOfWeek.name, days[4].toFloat()))
        chart.add(Pair(currentDay.minusDays(3).dayOfWeek.name, days[3].toFloat()))
        chart.add(Pair(currentDay.minusDays(2).dayOfWeek.name, days[2].toFloat()))
        chart.add(Pair(currentDay.minusDays(1).dayOfWeek.name, days[1].toFloat()))
        chart.add(Pair(currentDay.dayOfWeek.name, days[0].toFloat()))
    }
}