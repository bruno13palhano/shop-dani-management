package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var saleId = 0L
    private var productId = 0L
    private var stockId = 0L
    private var customerId = 0L
    private var customerName = ""
    private var photo = byteArrayOf()
    private var quantity = 0
    private var purchasePrice = 0F
    private var salePrice = 0F
    private var categories = listOf<Category>()
    private var company = ""
    private var dateOfSale = 0L
    private var dateOfPayment = 0L
    private var isOrderedByCustomer = false
    private var isPaidByCustomer = false
    private var canceled = false
    private var timestamp = getCurrentTimestamp()
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

    fun getDeliveryById(saleId: Long) {
        viewModelScope.launch {
            saleRepository.getById(saleId).collect {
                this@DeliveryViewModel.saleId = it.id
                productId = it.productId
                stockId = it.stockId
                customerId = it.customerId
                customerName = it.customerName
                photo = it.photo
                quantity = it.quantity
                purchasePrice = it.purchasePrice
                salePrice = it.salePrice
                dateOfSale = it.dateOfSale
                dateOfPayment = it.dateOfPayment
                isOrderedByCustomer = it.isOrderedByCustomer
                isPaidByCustomer = it.isPaidByCustomer
                canceled = it.canceled
                timestamp = it.timestamp
                name = it.customerName
                address = it.address
                phoneNumber = it.phoneNumber
                productName = it.name
                price = it.salePrice.toString()
                deliveryPrice = it.deliveryPrice.toString()
                categories = it.categories
                company = it.company
                shippingDate = it.shippingDate
                deliveryDate = it.deliveryDate
                delivered = it.delivered
            }
        }
    }

    fun updateDelivery(
        saleId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val sale = Sale(
            id = saleId,
            productId = productId,
            stockId = stockId,
            customerId = customerId,
            name = name,
            customerName = customerName,
            photo = photo,
            address = address,
            phoneNumber = phoneNumber,
            quantity = quantity,
            purchasePrice = purchasePrice,
            salePrice = salePrice,
            deliveryPrice = stringToFloat(deliveryPrice),
            categories = categories,
            company = company,
            dateOfSale = dateOfSale,
            dateOfPayment = dateOfPayment,
            shippingDate = shippingDate,
            deliveryDate = deliveryDate,
            isOrderedByCustomer = isOrderedByCustomer,
            isPaidByCustomer = isPaidByCustomer,
            delivered = delivered,
            canceled = canceled,
            timestamp = getCurrentTimestamp()
        )

        viewModelScope.launch {
            saleRepository.update(
                model = sale,
                onError = onError,
                onSuccess = onSuccess
            )
        }
    }
}