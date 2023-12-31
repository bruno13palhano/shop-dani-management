package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FinancialInfoViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository,
) : ViewModel() {
    val financial = saleRepository.getAll().map {
        var allSalesPurchasePrice = 0F
        var allSales = 0F
        var allDeliveriesPrice = 0F
        var stockSales = 0F
        var ordersSales = 0F

        it.map { sale ->
            allSales += sale.salePrice
            allSalesPurchasePrice += sale.purchasePrice

            if (sale.isOrderedByCustomer) {
                ordersSales += sale.salePrice
            } else {
                stockSales += sale.salePrice
            }
        }

        it.map { sale ->
            allDeliveriesPrice += sale.deliveryPrice
        }

        FinancialInfo(
            allSales = allSales,
            stockSales = stockSales,
            ordersSales = ordersSales,
            profit = allSales - (allSalesPurchasePrice + allDeliveriesPrice),
        )
    }
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

    data class FinancialInfo(
        val allSales: Float = 0F,
        val stockSales: Float = 0F,
        val ordersSales: Float = 0F,
        val profit: Float = 0F,
    )

    data class FinancialChartEntries(
        val allSalesEntries: Pair<Float, Float> = Pair(0F, 0F),
        val stockSalesEntries: Pair<Float, Float> = Pair(0F, 0F),
        val ordersSalesEntries: Pair<Float, Float> = Pair(0F, 0F),
        val profitEntries: Pair<Float, Float> = Pair(0F, 0F)
    )
}