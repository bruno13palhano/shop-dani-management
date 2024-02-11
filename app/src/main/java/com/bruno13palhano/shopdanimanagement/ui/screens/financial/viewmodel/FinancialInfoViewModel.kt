package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.Financial
import com.bruno13palhano.core.data.domain.FinancialUseCase
import com.bruno13palhano.core.model.FinancialInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FinancialInfoViewModel @Inject constructor(
    @Financial private val financialUseCase: FinancialUseCase
) : ViewModel() {
    val financial = financialUseCase()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = FinancialInfo()
        )

    val entry = financial
        .map {
            FinancialChartEntries(
                allSalesEntries = Pair(0F, it.allSales),
                stockSalesEntries = Pair(0F, it.stockSales),
                ordersSalesEntries = Pair(0F, it.ordersSales),
                profitEntries = Pair(0F, it.profit)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = FinancialChartEntries()
        )

    data class FinancialChartEntries(
        val allSalesEntries: Pair<Float, Float> = Pair(0F, 0F),
        val stockSalesEntries: Pair<Float, Float> = Pair(0F, 0F),
        val ordersSalesEntries: Pair<Float, Float> = Pair(0F, 0F),
        val profitEntries: Pair<Float, Float> = Pair(0F, 0F)
    )
}