package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.data.di.DeliveryRep
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    @DeliveryRep private val deliveryRepository: DeliveryRepository
) : ViewModel() {
    private var saleId = 0L
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
                saleId = it.saleId
                name = it.customerName
                address = it.address
                phoneNumber = it.phoneNumber
                productName = it.productName
                price = it.price.toString()
                deliveryPrice = it.deliveryPrice.toString()
                shippingDate = it.shippingDate
                deliveryDate = it.deliveryDate
                delivered = it.delivered
            }
        }
    }

    fun updateDelivery(
        deliveryId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val delivery = Delivery(
            id = deliveryId,
            saleId = saleId,
            customerName = name,
            address = address,
            phoneNumber = phoneNumber,
            productName = productName,
            price = stringToFloat(price),
            deliveryPrice = stringToFloat(deliveryPrice),
            shippingDate = shippingDate,
            deliveryDate = deliveryDate,
            delivered = delivered,
            timestamp = getCurrentTimestamp()
        )

        viewModelScope.launch {
            deliveryRepository.update(
                model = delivery,
                onError = onError,
                onSuccess = onSuccess
            )
        }
    }

    private fun stringToFloat(value: String): Float {
        return try {
            value.replace(",", ".").toFloat()
        } catch (ignored: Exception) { 0F }
    }
}