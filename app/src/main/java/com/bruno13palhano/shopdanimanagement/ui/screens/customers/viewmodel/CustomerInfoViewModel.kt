package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultCustomerRepository
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.SecondaryCustomerRepository
import com.bruno13palhano.core.data.di.SecondarySaleRepository
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
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
    @SecondaryCustomerRepository private val customerRepository: CustomerData<Customer>,
    @SecondarySaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val currentDay = LocalDate.now()

    private val _customerPurchases = MutableStateFlow(emptyList<Sale>())
    private val _customer = MutableStateFlow(Customer(0L, "", "", "", "", ""))

    val customerInfo = combine(_customerPurchases, _customer) { purchases, customer ->
        var owingValue = 0F
        var purchasesValue = 0F
        var lastPurchaseValue = 0F
        purchases.filter { !it.isPaidByCustomer }.map { owingValue += it.salePrice }
        purchases.map { purchasesValue += it.salePrice }
        purchases.map { sale -> if (sale.salePrice != 0F) lastPurchaseValue = sale.salePrice }

        CustomerInfo(
            name = customer.name,
            address = customer.address,
            photo = customer.photo,
            owingValue = owingValue.toString(),
            purchasesValue = purchasesValue.toString(),
            lastPurchaseValue = lastPurchaseValue.toString()
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = CustomerInfo()
        )

    private val _entry = MutableStateFlow(ChartEntryModelProducer())
    val entry = _entry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ChartEntryModelProducer()
        )

    fun getCustomerInfo(customerId: Long) {
        viewModelScope.launch {
            saleRepository.getByCustomerId(customerId).collect {
                _customerPurchases.value = it
            }
        }

        viewModelScope.launch {
            customerRepository.getById(customerId).collect {
                _customer.value = it
            }
        }
    }

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
                    _entry.value = it
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

    data class CustomerInfo(
        val name: String = "",
        val address: String = "",
        val photo: String = "",
        val owingValue: String = "",
        val purchasesValue: String = "",
        val lastPurchaseValue: String = "",
    )
}