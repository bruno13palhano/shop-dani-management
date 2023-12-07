package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(
    @CustomerRep private val customersRepository: CustomerRepository
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
}