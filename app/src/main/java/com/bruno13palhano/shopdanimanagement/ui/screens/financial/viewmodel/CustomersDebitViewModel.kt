package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SaleInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomersDebitViewModel
    @Inject
    constructor(
        @SaleRep private val saleRepository: SaleRepository
    ) : ViewModel() {
        private val _debits = MutableStateFlow(emptyList<Sale>())
        val debits =
            _debits
                .map {
                    it.map { sale ->
                        SaleInfo(
                            saleId = sale.id,
                            productId = sale.productId,
                            customerId = sale.customerId,
                            productName = sale.name,
                            customerName = sale.customerName,
                            productPhoto = sale.photo,
                            customerPhoto = byteArrayOf(),
                            address = sale.address,
                            phoneNumber = sale.phoneNumber,
                            email = "",
                            salePrice = sale.salePrice,
                            deliveryPrice = sale.deliveryPrice,
                            quantity = sale.quantity,
                            dateOfSale = sale.dateOfPayment
                        )
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

        fun getDebits() {
            viewModelScope.launch {
                viewModelScope.launch {
                    saleRepository.getDebitSales().collect {
                        _debits.value = it
                    }
                }
            }
        }

        fun getDebitByCustomerName(isOrderedAsc: Boolean) {
            viewModelScope.launch {
                saleRepository.getSalesByCustomerName(
                    isPaidByCustomer = false,
                    isOrderedAsc = isOrderedAsc
                ).collect {
                    _debits.value = it
                }
            }
        }

        fun getDebitBySalePrice(isOrderedAsc: Boolean) {
            viewModelScope.launch {
                saleRepository.getSalesBySalePrice(
                    isPaidByCustomer = false,
                    isOrderedAsc = isOrderedAsc
                ).collect {
                    _debits.value = it
                }
            }
        }
    }