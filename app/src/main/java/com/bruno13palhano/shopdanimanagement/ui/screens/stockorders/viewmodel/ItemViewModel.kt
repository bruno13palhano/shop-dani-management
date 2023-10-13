package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductData<Product>,
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private var productId by mutableLongStateOf(0L)
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf(byteArrayOf())
        private set
    var date by mutableLongStateOf(0L)
        private set
    var quantity by mutableStateOf("")
        private set
    var validity by mutableLongStateOf(0L)
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var salePrice by mutableStateOf("")
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    var isPaid by mutableStateOf(false)
        private set

    val isItemNotEmpty = snapshotFlow {
        name.isNotEmpty() && quantity.isNotEmpty() && purchasePrice.isNotEmpty()
                && salePrice.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateDate(date: Long) {
        this.date = date
    }

    fun updateValidity(validity: Long) {
        this.validity = validity
    }

    fun updatePurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun updateSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun updateIsPaid(isPaid: Boolean) {
        this.isPaid = isPaid
    }

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                name = it.name
                photo = it.photo
                updateDate(it.date)
                company = it.company
                category = setCategories(it.categories)
                company = it.company
            }
        }
    }

    private fun setCategories(categories: List<Category>) =
        categories.joinToString(", ") { category -> category.name }

    fun insertItems(productId: Long, isOrderedByCustomer: Boolean) {
        viewModelScope.launch {
            stockRepository.insert(createStockOrder(productId, isOrderedByCustomer))
        }
    }

    fun getStockOrder(stockOrderItemId: Long) {
        viewModelScope.launch {
            stockRepository.getById(stockOrderItemId).collect {
                productId = it.productId
                name = it.name
                photo = it.photo
                quantity = it.quantity.toString()
                company = it.company
                date = it.date
                validity = it.validity
                category = setCategories(it.categories)
                company = it.company
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                isPaid = it.isPaid
            }
        }
    }

    fun updateStockOrderItem(stockOrderItemId: Long, isOrderedByCustomer: Boolean) {
        viewModelScope.launch {
            stockRepository.update(updateStockOrder(stockOrderItemId, isOrderedByCustomer))
        }
    }

    fun deleteStockOrderItem(stockOrderId: Long) {
        viewModelScope.launch {
            stockRepository.deleteById(id = stockOrderId)
        }
    }

    private fun createStockOrder(productId: Long, isOrderedByCustomer: Boolean) = StockOrder(
        id = 0L,
        productId = productId,
        name = name,
        photo = photo,
        quantity = quantity.toInt(),
        date = date,
        validity = validity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )

    private fun updateStockOrder(stockOrderId: Long, isOrderedByCustomer: Boolean) = StockOrder(
        id = stockOrderId,
        productId = productId,
        name = name,
        photo = photo,
        quantity = quantity.toInt(),
        date = date,
        validity = validity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )
}