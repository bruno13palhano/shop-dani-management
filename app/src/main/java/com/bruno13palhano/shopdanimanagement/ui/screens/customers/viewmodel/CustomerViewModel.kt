package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.di.DefaultCustomerRepository
import com.bruno13palhano.core.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    @DefaultCustomerRepository private val customerRepository: CustomerData<Customer>
) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set

    val isCustomerNotEmpty = snapshotFlow {
        name.isNotEmpty() && address.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateName(name: String) {
        this.name = name
    }

    fun updatePhoto(photo: String) {
        this.photo = photo
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateAddress(address: String) {
        this.address = address
    }

    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun getCustomer(id: Long) {
        viewModelScope.launch {
            customerRepository.getById(id).collect {
                name = it.name
                photo = it.photo
                email = it.email
                address = it.address
                phoneNumber = it.phoneNumber
            }
        }
    }

    fun insertCustomer() {
        val customer = Customer(
            id = 0L,
            name = name,
            photo = photo,
            email = email,
            address = address,
            phoneNumber = phoneNumber
        )
        viewModelScope.launch {
            customerRepository.insert(customer)
        }
    }

    fun updateCustomer(id: Long) {
        val customer = Customer(
            id = id,
            name = name,
            photo = photo,
            email = email,
            address = address,
            phoneNumber = phoneNumber
        )
        viewModelScope.launch {
            customerRepository.update(customer)
        }
    }
}