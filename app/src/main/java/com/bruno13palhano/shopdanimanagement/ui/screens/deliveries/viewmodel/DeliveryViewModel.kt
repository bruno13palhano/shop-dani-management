package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.DeliveryRep
import com.bruno13palhano.core.model.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    @DeliveryRep private val deliveryRepository: DeliveryData<Delivery>
) : ViewModel() {
    private var deliveryPriceDb = 0F
    private var shippingDateDb = 0L
    private var deliveryDateDb = 0L
    private var deliveredDb = false

    var name by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set
    var productName by mutableStateOf("")
        private set
    var price by mutableStateOf("")
        private set
    var deliveryPrice by mutableStateOf("")
        private set
    var shippingDate by mutableLongStateOf(0L)
        private set
    var deliveryDate by mutableLongStateOf(0L)
        private set
    var delivered by mutableStateOf(false)
        private set

    fun updateDeliveryPrice(deliveryPrice: String) {
        this.deliveryPrice = deliveryPrice
    }

    fun updateShippingDate(shippingDate: Long) {
        this.shippingDate = shippingDate
    }

    fun updateDeliveryDate(deliveryDate: Long) {
        this.deliveryDate = deliveryDate
    }

    fun updateDelivered(delivered: Boolean) {
        this.delivered = delivered
    }

    fun getDeliveryById(deliveryId: Long) {
        viewModelScope.launch {
            deliveryRepository.getById(deliveryId).collect {
                name = it.customerName
                address = it.address
                phoneNumber = it.phoneNumber
                productName = it.productName
                price = it.price.toString()
                deliveryPrice = it.deliveryPrice.toString()
                shippingDate = it.shippingDate
                shippingDateDb = it.shippingDate
                deliveryDate = it.deliveryDate
                deliveryPriceDb = it.deliveryPrice
                deliveryDateDb = it.deliveryDate
                delivered = it.delivered
                deliveredDb = it.delivered
            }
        }
    }

    fun updateDelivery(deliveryId: Long) {
        if (stringToFloat(deliveryPrice) != deliveryPriceDb) {
            viewModelScope.launch {
                deliveryRepository.updateDeliveryPrice(deliveryId, stringToFloat(deliveryPrice))
            }
        }
        if (shippingDate != shippingDateDb) {
            viewModelScope.launch {
                deliveryRepository.updateShippingDate(deliveryId, shippingDate)
            }
        }
        if (deliveryDate != deliveryDateDb) {
            viewModelScope.launch {
                deliveryRepository.updateDeliveryDate(deliveryId, deliveryDate)
            }
        }
        if (delivered != deliveredDb) {
            viewModelScope.launch {
                deliveryRepository.updateDelivered(deliveryId, delivered)
            }
        }
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.replace(",", ".").toFloat()
        } catch (ignored: Exception) { 0F }
    }
}