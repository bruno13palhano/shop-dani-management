package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.DefaultStockOrderRepository
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditStockItemViewModel @Inject constructor(
    @DefaultStockOrderRepository private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private var productId by mutableLongStateOf(0L)
    var name by mutableStateOf("")
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var date: String by mutableStateOf(dateFormat.format(dateInMillis))
        private set
    var quantity by mutableStateOf("")
        private set

    val isStockItemNotEmpty = snapshotFlow {
        purchasePrice != "" && quantity != ""
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

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

    fun getStockItem(id: Long) {
        viewModelScope.launch {
            stockRepository.getById(id).collect {
                productId = it.productId
                name = it.name
                purchasePrice = it.purchasePrice.toString()
                photo = it.photo
                quantity = it.quantity.toString()
                updateDate(it.date)
            }
        }
    }

    fun updateStockItem(id: Long) {
        val stockItem = StockOrder(
            id = id,
            productId = productId,
            name = name,
            photo = photo,
            purchasePrice = stringToFloat(purchasePrice),
            date = dateInMillis,
            quantity = quantity.toInt(),
            isOrderedByCustomer = false
        )
        viewModelScope.launch {
            stockRepository.update(stockItem)
        }
    }
}