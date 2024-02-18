package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.CustomerInformation
import com.bruno13palhano.core.data.domain.CustomerInfoUseCase
import com.bruno13palhano.core.model.CustomerInfo
import com.bruno13palhano.shopdanimanagement.ui.screens.setChartEntries
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerInfoViewModel @Inject constructor(
    @CustomerInformation private val customerInfoUseCase: CustomerInfoUseCase
) : ViewModel() {
    private var _customerInfo = MutableStateFlow(CustomerInfo.emptyCustomerInfo())
     val customerInfo = _customerInfo
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = CustomerInfo.emptyCustomerInfo()
        )

    private val _entry = MutableStateFlow(listOf<Pair<String, Float>>())
    val entry = _entry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = listOf()
        )

    fun getCustomerInfo(customerId: Long) {
        viewModelScope.launch {
            customerInfoUseCase.getCustomerInfo(customerId = customerId).collect {
                _customerInfo.value = it
            }
        }
    }

    fun getCustomerPurchases(customerId: Long) {
        val days = Array(31) { 0 }
        val chartEntries = mutableListOf<Pair<String, Float>>()

        viewModelScope.launch {
            customerInfoUseCase.getCustomerSales(customerId = customerId)
                .map {
                    it.map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                    setChartEntries(chartEntries, days)

                    chartEntries
                }
                .collect {
                    _entry.value = it
                }
        }
    }
}