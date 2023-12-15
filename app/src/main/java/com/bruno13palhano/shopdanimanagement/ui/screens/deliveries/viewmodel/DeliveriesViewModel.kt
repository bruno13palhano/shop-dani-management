package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var _deliveries = MutableStateFlow(emptyList<Delivery>())
    val deliveries = _deliveries
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getAllDeliveries() {
        viewModelScope.launch {
            saleRepository.getAll().collect {
                _deliveries.value = it.map { sale ->
                    Delivery(
                        id = sale.id,
                        saleId = sale.id,
                        customerName = sale.customerName,
                        address = sale.address,
                        phoneNumber = sale.phoneNumber,
                        productName = sale.name,
                        price = sale.salePrice,
                        deliveryPrice = sale.deliveryPrice,
                        shippingDate = sale.shippingDate,
                        deliveryDate = sale.deliveryDate,
                        delivered = sale.delivered,
                        timestamp = sale.timestamp
                    )
                }
            }
        }
    }

    fun getDeliveries(delivered: Boolean) {
        viewModelScope.launch {
            saleRepository.getDeliveries(delivered = delivered).collect {
                _deliveries.value = it.map { sale ->
                    Delivery(
                        id = sale.id,
                        saleId = sale.id,
                        customerName = sale.customerName,
                        address = sale.address,
                        phoneNumber = sale.phoneNumber,
                        productName = sale.name,
                        price = sale.salePrice,
                        deliveryPrice = sale.deliveryPrice,
                        shippingDate = sale.shippingDate,
                        deliveryDate = sale.deliveryDate,
                        delivered = sale.delivered,
                        timestamp = sale.timestamp
                    )
                }
            }
        }
    }
}