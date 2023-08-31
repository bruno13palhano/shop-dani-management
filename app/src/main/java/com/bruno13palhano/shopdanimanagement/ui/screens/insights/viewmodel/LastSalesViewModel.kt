package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class LastSalesViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val currentDay = LocalDate.now()
    val lastSalesEntry = saleRepository.getLastSales(0, 100)
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
            chart.add(Pair(currentDay.minusDays(i.toLong()).dayOfWeek.name, days[i].toFloat()))
        }
    }
}