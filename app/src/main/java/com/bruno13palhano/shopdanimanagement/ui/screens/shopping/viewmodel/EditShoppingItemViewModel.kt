package com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.data.di.SecondaryProductRepository
import com.bruno13palhano.core.data.di.SecondaryShoppingRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Shopping
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditShoppingItemViewModel @Inject constructor(
    @SecondaryShoppingRepository private val shoppingRepository: ShoppingData<Shopping>,
    @SecondaryProductRepository private val productRepository: ProductData<Product>
) : ViewModel() {
    var productId by mutableLongStateOf(0L)
        private set
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

    fun updateName(name: String) {
        this.name = name
    }

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
            shoppingRepository.getById(shoppingItemId).collect {
                name = it.name
                purchasePrice = it.purchasePrice.toString()
                quantity = it.quantity.toString()
                isPaid = it.isPaid
                updateDate(it.date)
                productId = it.productId
            }
        }
    }

    fun getPhoto(productId: Long) {
        viewModelScope.launch {
            productRepository.getById(productId).collect {
                photo = it.photo
            }
        }
    }

    fun updateShoppingItem(id: Long) {
        val shoppingItem = Shopping(
            id = id,
            productId = productId,
            name = name,
            purchasePrice = stringToFloat(purchasePrice),
            quantity = quantity.toInt(),
            date = dateInMillis,
            isPaid = isPaid
        )
        viewModelScope.launch {
            shoppingRepository.insert(shoppingItem)
        }
    }
}