package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.ShoppingRep
import com.bruno13palhano.core.model.Shopping
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
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ShoppingItemsViewModel @Inject constructor(
    @ShoppingRep private val shoppingRepository: ShoppingData<Shopping>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

    private val _shoppingItems = MutableStateFlow(ChartEntryModelProducer() + ChartEntryModelProducer())
    val shoppingItems = _shoppingItems
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer() + ChartEntryModelProducer()
        )

    fun setChartRangeOfDays(rangeOfDays: Int) {
        val paidChart = mutableListOf<Pair<String, Float>>()
        val owingChart = mutableListOf<Pair<String, Float>>()
        days = Array(rangeOfDays) { 0 }

        viewModelScope.launch {
            shoppingRepository.getItemsLimited(0, 100)
                .map {
                    it.filter { item -> item.isPaid }
                        .map { item -> setQuantityOfItemsInTheDay(days, item.date, item.quantity) }
                    setChartEntries(paidChart, days)

                    it.filter { item -> !item.isPaid }
                        .map { item -> setQuantityOfItemsInTheDay(days, item.date, item.quantity) }
                    setChartEntries(owingChart, days)

                    ChartEntryModelProducer(paidChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y) }
                    ) +
                    ChartEntryModelProducer(owingChart.mapIndexed { index, (date, y) ->
                        DateChartEntry(date, index.toFloat(), y) }
                    )
                }
                .collect {
                    _shoppingItems.value = it
                }
        }
    }

    private fun setQuantityOfItemsInTheDay(days: Array<Int>, date: Long, quantity: Int) {
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