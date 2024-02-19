package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    @CustomerRep private val customerRepository: CustomerRepository
) : ViewModel() {
    private var _customerState = MutableStateFlow<UiState>(UiState.Fail)
    val customerState = _customerState.asStateFlow()

    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf(byteArrayOf())
        private set
    var email by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var city by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set
    var gender by mutableStateOf("")
        private set
    var age by mutableStateOf("")
        private set

    val isCustomerNotEmpty = snapshotFlow {
        name.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateName(name: String) {
        this.name = name
    }

    fun updatePhoto(photo: ByteArray) {
        this.photo = photo
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateAddress(address: String) {
        this.address = address
    }

    fun updateCity(city: String) {
        this.city = city
    }

    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun updateGender(gender: String) {
        this.gender = gender
    }

    fun updateAge(age: String) {
        this.age = age
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

    fun insertCustomer(onError: (error: Int) -> Unit) {
        _customerState.value = UiState.InProgress
        val customer = Customer(
            id = 0L,
            name = name,
            photo = photo,
            email = email,
            address = address,
            city = city,
            phoneNumber = phoneNumber,
            gender = gender,
            age = stringToInt(age),
            timestamp = getCurrentTimestamp()
        )
        viewModelScope.launch {
            customerRepository.insert(
                model = customer,
                onError = {
                    onError(it)
                    _customerState.value = UiState.Fail
                },
                onSuccess = { _customerState.value = UiState.Success }
            )
        }
    }

    fun updateCustomer(id: Long, onError: (error: Int) -> Unit) {
        _customerState.value = UiState.InProgress
        val customer = Customer(
            id = id,
            name = name,
            photo = photo,
            email = email,
            address = address,
            city = city,
            phoneNumber = phoneNumber,
            gender = gender,
            age = stringToInt(age),
            timestamp = getCurrentTimestamp()
        )
        viewModelScope.launch {
            customerRepository.update(
                model = customer,
                onError = {
                    onError(it)
                    _customerState.value = UiState.Fail
                },
                onSuccess = { _customerState.value = UiState.Success }
            )
        }
    }
}