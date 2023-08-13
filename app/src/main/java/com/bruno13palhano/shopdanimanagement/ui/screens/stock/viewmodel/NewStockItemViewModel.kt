package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.data.di.DefaultStockOrderRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.currentDate
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewStockItemViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultStockOrderRepository private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private var productId by mutableLongStateOf(0L)
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(currentDate)
        private set
    var date: String by mutableStateOf(dateFormat.format(dateInMillis))
        private set
    var quantity by mutableStateOf("")
        private set
    var isOrderedByCustomer by mutableStateOf(false)
        private set

    fun updatePurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = dateFormat.format(dateInMillis)
    }

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateIsOrderedByCustomer(isOrderedByCustomer: Boolean) {
        this.isOrderedByCustomer = isOrderedByCustomer
    }

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                productId = it.id
                name = it.name
                photo = it.photo
                dateInMillis = it.date
            }
        }
    }

    fun insertStock() {
        val stockItem = StockOrder(
            id = 0L,
            productId = productId,
            name = name,
            photo = photo,
            purchasePrice = stringToFloat(purchasePrice),
            date = dateInMillis,
            quantity = quantity.toInt(),
            isOrderedByCustomer = isOrderedByCustomer
        )
        viewModelScope.launch {
            stockRepository.insert(stockItem)
        }
    }
}