package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.Shopping
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FinancialInfoViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>,
    @DefaultShoppingRepository private val shoppingRepository: ShoppingData<Shopping>
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

    data class FinancialInfo(
        val allSales: Float = 0F,
        val stockSales: Float = 0F,
        val ordersSales: Float = 0F,
        val profit: Float = 0F,
        val shopping: Float = 0F
    )
}