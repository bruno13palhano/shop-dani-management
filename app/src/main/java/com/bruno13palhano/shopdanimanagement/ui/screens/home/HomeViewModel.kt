package com.bruno13palhano.shopdanimanagement.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
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
    @SaleRep private val saleRepository: SaleData<Sale>,
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private val currentDay = LocalDate.now()

    val homeInfo = saleRepository.getAll().map { sales ->
        var salesPrice = 0F
        var purchasePrice = 0F
        var biggestSale = Info()
        var biggestSaleValue = 0F
        var smallestSale = Info()
        var smallestSaleValue = Float.MAX_VALUE
        var lastSale = Info()

        sales.map { sale ->
            salesPrice += sale.salePrice
            purchasePrice += sale.purchasePrice
            if (biggestSaleValue <= sale.salePrice) {
                biggestSaleValue = sale.salePrice
                biggestSale = Info(
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = dateFormat.format(sale.dateOfSale)
                )
            }
            if (smallestSaleValue >= sale.salePrice) {
                smallestSaleValue = sale.salePrice
                smallestSale = Info(
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = dateFormat.format(sale.dateOfSale)
                )
            }
            if (sales.lastIndex != -1) {
                val last = sales.last()
                lastSale = Info(
                    value = (last.quantity * last.salePrice),
                    customer = last.customerName,
                    item = last.name,
                    quantity = last.quantity,
                    date = dateFormat.format(last.dateOfSale)
                )
            }
        }
        HomeInfo(
            sales = salesPrice,
            profit = salesPrice - purchasePrice,
            biggestSale = biggestSale,
            smallestSale = smallestSale,
            lastSale = lastSale
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = HomeInfo()
        )

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

    data class HomeInfo(
        val profit: Float = 0F,
        val sales: Float = 0F,
        val biggestSale: Info = Info(),
        val smallestSale: Info = Info(),
        val lastSale: Info = Info()
    )

    data class Info(
        val value: Float = 0F,
        val customer: String = "",
        val item: String = "",
        val quantity: Int = 0,
        val date: String = ""
    )
}