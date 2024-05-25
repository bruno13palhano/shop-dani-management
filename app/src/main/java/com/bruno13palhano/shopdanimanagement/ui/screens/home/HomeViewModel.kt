package com.bruno13palhano.shopdanimanagement.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginState
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository,
    @UserRep private val userRepository: UserRepository
) : ViewModel() {
    private var _loginState = MutableStateFlow<LoginState>(LoginState.InProgress)
    val loginState = _loginState.asStateFlow()
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
            salesPrice += (sale.salePrice * sale.quantity)
            purchasePrice += (sale.purchasePrice * sale.quantity)
            if (biggestSaleValue <= (sale.salePrice * sale.quantity)) {
                biggestSaleValue = (sale.salePrice * sale.quantity)
                biggestSale = Info(
                    id = sale.id,
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = sale.dateOfSale
                )
            }
            if (smallestSaleValue >= (sale.salePrice * sale.quantity)) {
                smallestSaleValue = (sale.salePrice * sale.quantity)
                smallestSale = Info(
                    id = sale.id,
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = sale.dateOfSale
                )
            }
            if (sales.lastIndex != -1) {
                val last = sales.last()
                lastSale = Info(
                    id = last.id,
                    value = (last.quantity * last.salePrice),
                    customer = last.customerName,
                    item = last.name,
                    quantity = last.quantity,
                    date = last.dateOfSale
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

            it.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
            setChartEntries(chart, days)

            chart.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun isAuthenticated() {
        viewModelScope.launch {
            userRepository.authenticated().collect {
                if (it) {
                    _loginState.value = LoginState.SignedIn
                } else {
                    _loginState.value = LoginState.SignedOut
                }
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
        val id: Long = 0L,
        val value: Float = 0F,
        val customer: String = "",
        val item: String = "",
        val quantity: Int = 0,
        val date: Long = 0L
    )
}