package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Sale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SalesByCompanyViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private var days = arrayOf(0)
    private val currentDay = LocalDate.now()

    private val _chartEntry = MutableStateFlow(SalesCompanyEntries())
    val chartEntry = _chartEntry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = SalesCompanyEntries()
        )

    fun getChartByRange(rangeOfDays: Int) {
        viewModelScope.launch {
            saleRepository.getAll().collect {
                val avonEntries = mutableListOf<Pair<String, Float>>()
                val naturaEntries = mutableListOf<Pair<String, Float>>()

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.AVON.company }
                    .map { sale -> setDay(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(avonEntries, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.NATURA.company }
                    .map { sale -> setDay(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(naturaEntries, days)

                _chartEntry.value = SalesCompanyEntries(
                    avonEntries = avonEntries,
                    naturaEntries = naturaEntries
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

    data class SalesCompanyEntries(
        val avonEntries: List<Pair<String, Float>> = listOf(),
        val naturaEntries: List<Pair<String, Float>> = listOf()
    )
}