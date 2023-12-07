package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.setChartEntries
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class CustomerInfoViewModel @Inject constructor(
    @CustomerRep private val customerRepository: CustomerData<Customer>,
    @SaleRep private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val _customerPurchases = MutableStateFlow(emptyList<Sale>())
    private val _customer = MutableStateFlow(Customer(0L, "", byteArrayOf(), "", "", "", OffsetDateTime.now(ZoneOffset.UTC)))

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

    private val _entry = MutableStateFlow(listOf<Pair<String, Float>>())
    val entry = _entry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = listOf()
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

                    chartEntries
                }
                .collect {
                    _entry.value = it
                }
        }
    }

    data class CustomerInfo(
        val name: String = "",
        val address: String = "",
        val photo: ByteArray = byteArrayOf(),
        val owingValue: String = "",
        val purchasesValue: String = "",
        val lastPurchaseValue: String = "",
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CustomerInfo

            if (name != other.name) return false
            if (address != other.address) return false
            if (!photo.contentEquals(other.photo)) return false
            if (owingValue != other.owingValue) return false
            if (purchasesValue != other.purchasesValue) return false
            if (lastPurchaseValue != other.lastPurchaseValue) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + address.hashCode()
            result = 31 * result + photo.contentHashCode()
            result = 31 * result + owingValue.hashCode()
            result = 31 * result + purchasesValue.hashCode()
            result = 31 * result + lastPurchaseValue.hashCode()
            return result
        }
    }
}