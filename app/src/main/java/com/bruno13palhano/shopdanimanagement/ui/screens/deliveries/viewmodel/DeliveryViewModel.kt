package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.DefaultDeliveryRepository
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    @DefaultDeliveryRepository private val deliveryRepository: DeliveryData<Delivery>
) : ViewModel() {
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
    var shippingDateInMillis by mutableLongStateOf(0L)
        private set
    var shippingDate by mutableStateOf("")
        private set
    var deliveryDateInMillis by mutableLongStateOf(0L)
        private set
    var deliveryDate by mutableStateOf("")
        private set
    var delivered by mutableStateOf(false)
        private set

    fun updateShippingDate(shippingDate: Long) {
        shippingDateInMillis = shippingDate
        this.shippingDate = dateFormat.format(shippingDateInMillis)
    }

    fun updateDeliveryDate(deliveryDate: Long) {
        deliveryDateInMillis = deliveryDate
        this.deliveryDate = dateFormat.format(deliveryDateInMillis)
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
                updateShippingDate(it.shippingDate)
                shippingDateDb = it.shippingDate
                updateDeliveryDate(it.deliveryDate)
                deliveryDateDb = it.deliveryDate
                delivered = it.delivered
                deliveredDb = it.delivered
            }
        }
    }

    fun updateDelivery(deliveryId: Long) {
        if (shippingDateInMillis != shippingDateDb) {
            viewModelScope.launch {
                deliveryRepository.updateShippingDate(deliveryId, shippingDateInMillis)
            }
        }
        if (deliveryDateInMillis != deliveryDateDb) {
            viewModelScope.launch {
                deliveryRepository.updateDeliveryDate(deliveryId, deliveryDateInMillis)
            }
        }
        if (delivered != deliveredDb) {
            viewModelScope.launch {
                deliveryRepository.updateDelivered(deliveryId, delivered)
            }
        }
    }
}