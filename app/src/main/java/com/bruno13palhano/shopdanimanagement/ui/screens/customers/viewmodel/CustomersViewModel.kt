package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.CustomerInformation
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.domain.CustomerInfoUseCase
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.CustomerInfo
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
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
class CustomersViewModel @Inject constructor(
    @CustomerRep private val customersRepository: CustomerRepository,
    @CustomerInformation private val customerInfoUseCase: CustomerInfoUseCase
): ViewModel() {
    private val _customerList = MutableStateFlow(emptyList<Customer>())
    val customerList = _customerList
        .map {
            it.map { customer ->
                CommonItem(
                    id = customer.id,
                    photo = customer.photo,
                    title = customer.name,
                    subtitle = customer.address,
                    description = customer.email
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

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

    fun getAllCustomers() {
        viewModelScope.launch {
            customersRepository.getAll().collect {
                _customerList.value = it
            }
        }
    }

    fun getOrderedByName(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            customersRepository.getOrderedByName(isOrderedAsc = isOrderedAsc).collect {
                _customerList.value = it
            }
        }
    }

    fun getOrderedByAddress(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            customersRepository.getOrderedByAddress(isOrderedAsc = isOrderedAsc).collect {
                _customerList.value = it
            }
        }
    }

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