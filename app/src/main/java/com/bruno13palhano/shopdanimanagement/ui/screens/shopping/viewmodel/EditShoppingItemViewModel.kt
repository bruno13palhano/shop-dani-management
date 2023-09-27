package com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditShoppingItemViewModel @Inject constructor(
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private var stockItemId by mutableLongStateOf(0L)
    private var productId by mutableLongStateOf(0L)
    private var validity by mutableLongStateOf(0L)
    private var categories by mutableStateOf(listOf<Category>())
    private var company by mutableStateOf("")
    private var salePrice by mutableFloatStateOf(0F)
    private var isOrderedByCustomer by mutableStateOf(false)

    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var date by mutableStateOf("")
        private set
    var quantity by mutableStateOf("")
        private set
    var isPaid by mutableStateOf(false)
        private set

    val isItemNotEmpty = snapshotFlow {
        name.isNotEmpty() && purchasePrice.isNotEmpty() && date.isNotEmpty() && quantity.isNotEmpty()
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
        this.dateInMillis = date
        this.date = dateFormat.format(dateInMillis)
    }

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateIsPaid(isPaid: Boolean) {
        this.isPaid = isPaid
    }

    fun getShoppingItem(shoppingItemId: Long) {
        viewModelScope.launch {
            stockRepository.getById(shoppingItemId).collect {
                stockItemId = it.id
                productId = it.productId
                name = it.name
                photo = it.photo
                updateDate(it.date)
                validity = it.validity
                quantity = it.quantity.toString()
                categories = it.categories
                company = it.company
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice
                isOrderedByCustomer = it.isOrderedByCustomer
                isPaid = it.isPaid
            }
        }
    }

    fun updateShoppingItem() {
        viewModelScope.launch {
            stockRepository.update(createShoppingItem())
        }
    }

    fun deleteShoppingItem() {
        viewModelScope.launch {
            stockRepository.deleteById(id = stockItemId)
        }
    }

    private fun createShoppingItem() = StockOrder(
        id = stockItemId,
        productId = productId,
        name = name,
        photo = photo,
        date = dateInMillis,
        validity = validity,
        quantity = quantity.toInt(),
        categories = categories,
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = salePrice,
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )
}