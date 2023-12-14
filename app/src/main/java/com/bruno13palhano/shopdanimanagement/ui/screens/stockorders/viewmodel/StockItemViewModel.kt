package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.stockorder.StockRepository
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.StockRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockItemViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @StockRep private val stockRepository: StockRepository
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
        quantity.isNotEmpty() && purchasePrice.isNotEmpty() && salePrice.isNotEmpty()
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
            }
        }
    }

    private fun setCategories(categories: List<Category>) =
        categories.joinToString(", ") { category -> category.category }

    fun insertItems(
        productId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            stockRepository.insert(
                model = createStockItem(productId),
                onError = onError,
                onSuccess = { onSuccess() }
            )
        }
    }

    fun getStockItem(stockItemId: Long) {
        viewModelScope.launch {
            stockRepository.getById(stockItemId).collect {
                productId = it.productId
                name = it.name
                photo = it.photo
                quantity = it.quantity.toString()
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

    fun updateStockItem(
        stockItemId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            stockRepository.update(
                model = updateStockItem(stockItemId),
                onError = onError,
                onSuccess = onSuccess
            )
        }
    }

    fun deleteStockItem(
        stockOrderId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            stockRepository.deleteById(
                id = stockOrderId,
                timestamp = getCurrentTimestamp(),
                onError = onError,
                onSuccess = onSuccess
            )
        }
    }

    private fun createStockItem(productId: Long) = StockItem(
        id = 0L,
        productId = productId,
        name = name,
        photo = photo,
        quantity = stringToInt(quantity),
        date = date,
        validity = validity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isPaid = isPaid,
        timestamp = getCurrentTimestamp()
    )

    private fun updateStockItem(stockOrderId: Long) = StockItem(
        id = stockOrderId,
        productId = productId,
        name = name,
        photo = photo,
        quantity = stringToInt(quantity),
        date = date,
        validity = validity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isPaid = isPaid,
        timestamp = getCurrentTimestamp()
    )

    private fun stringToFloat(value: String): Float {
        return try {
            value.replace(",", ".").toFloat()
        } catch (ignored: Exception) { 0F }
    }

    private fun stringToInt(value: String): Int {
        return try {
            value.toInt()
        } catch (ignored: Exception) { 0 }
    }
}