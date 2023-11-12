package com.bruno13palhano.shopdanimanagement.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.CatalogRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ExtendedItem
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>,
    @CatalogRep private val catalogRepository: CatalogData<Catalog>
) : ViewModel() {
    private val currentDay = LocalDate.now()

    val catalogItems = catalogRepository.getAll()
        .map {
            it.map { catalogItem ->
                ExtendedItem(
                    id = catalogItem.id,
                    photo = catalogItem.photo,
                    title = catalogItem.title,
                    firstSubtitle = catalogItem.name,
                    secondSubtitle = catalogItem.price.toString(),
                    description = catalogItem.description,
                    footer = catalogItem.discount.toString()
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

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
                    id = sale.id,
                    isOrderedByCustomer = sale.isOrderedByCustomer,
                    value = (sale.quantity * sale.salePrice),
                    customer = sale.customerName,
                    item = sale.name,
                    quantity = sale.quantity,
                    date = sale.dateOfSale
                )
            }
            if (smallestSaleValue >= sale.salePrice) {
                smallestSaleValue = sale.salePrice
                smallestSale = Info(
                    id = sale.id,
                    isOrderedByCustomer = sale.isOrderedByCustomer,
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
                    isOrderedByCustomer = last.isOrderedByCustomer,
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

            chart
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = listOf()
        )

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
        val isOrderedByCustomer: Boolean = false,
        val value: Float = 0F,
        val customer: String = "",
        val item: String = "",
        val quantity: Int = 0,
        val date: Long = 0L
    )
}