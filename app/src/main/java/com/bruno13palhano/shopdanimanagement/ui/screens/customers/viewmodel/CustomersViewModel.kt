package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.di.DefaultCustomerRepository
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(
    @DefaultCustomerRepository private val customersRepository: CustomerData<Customer>
): ViewModel() {
    val customerList = customersRepository.getAll()
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
}