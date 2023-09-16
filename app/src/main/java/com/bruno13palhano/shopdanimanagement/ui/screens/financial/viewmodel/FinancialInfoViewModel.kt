package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.data.di.SecondarySaleRepository
import com.bruno13palhano.core.data.di.SecondaryShoppingRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.Shopping
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FinancialInfoViewModel @Inject constructor(
    @SecondarySaleRepository private val saleRepository: SaleData<Sale>,
    @SecondaryShoppingRepository private val shoppingRepository: ShoppingData<Shopping>
) : ViewModel() {
    val financial = combine(saleRepository.getAll(), shoppingRepository.getAll()) { sale, shopping ->
        var allSalesPurchasePrice = 0F
        var allSales = 0F
        var stockSales = 0F
        var ordersSales = 0F
        var allShopping = 0F

        sale.map {
            allSales += it.salePrice
            allSalesPurchasePrice += it.purchasePrice

            if (it.isOrderedByCustomer) {
                ordersSales += it.salePrice
            } else {
                stockSales += it.salePrice
            }
        }

        shopping.map {
            allShopping += it.purchasePrice
        }

        FinancialInfo(
            allSales = allSales,
            stockSales = stockSales,
            ordersSales = ordersSales,
            profit = allSales - allSalesPurchasePrice,
            shopping = allShopping
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = FinancialInfo()
        )

    val entry = financial
        .map {
            ChartEntryModelProducer(
                listOf(
                    listOf(FloatEntry(0F, it.allSales)),
                    listOf(FloatEntry(0F, it.stockSales)),
                    listOf(FloatEntry(0F, it.ordersSales)),
                    listOf(FloatEntry(0F, it.profit)),
                    listOf(FloatEntry(0F, it.shopping))
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    data class FinancialInfo(
        val allSales: Float = 0F,
        val stockSales: Float = 0F,
        val ordersSales: Float = 0F,
        val profit: Float = 0F,
        val shopping: Float = 0F
    )
}