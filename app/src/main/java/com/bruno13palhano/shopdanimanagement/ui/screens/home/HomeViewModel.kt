package com.bruno13palhano.shopdanimanagement.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    val lastSales = saleRepository.getLastSales(0, 6)
        .map {
            val chart = mutableListOf<Pair<String, Float>>()
            it.map { sale ->
                chart.add(Pair(dateFormat.format(sale.dateOfSale), sale.salePrice))
            }

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
}