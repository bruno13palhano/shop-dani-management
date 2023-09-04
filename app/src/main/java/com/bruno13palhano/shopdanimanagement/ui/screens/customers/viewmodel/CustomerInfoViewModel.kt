package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
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
class CustomerInfoViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val currentDay = LocalDate.now()

    private val _customerPurchases = MutableStateFlow(ChartEntryModelProducer())
    val entry = _customerPurchases
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    fun getCustomerPurchases(customerId: Long) {
        val days = Array(31) { 0 }
        val chartEntries = mutableListOf<Pair<String, Float>>()

        viewModelScope.launch {
            saleRepository.getByCustomerId(customerId)
                .map {
                    it.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(chartEntries, days)

                    ChartEntryModelProducer(
                        chartEntries.mapIndexed { index, (date, y) ->
                            DateChartEntry(date, index.toFloat(), y)
                        }
                    )
                }
                .collect {
                    _customerPurchases.value = it
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