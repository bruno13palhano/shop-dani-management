package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.StockRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockItemViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @StockRep private val stockRepository: StockRepository
) : ViewModel() {
    private var _stockItemState = MutableStateFlow<UiState>(UiState.Fail)
    val stockItemState = _stockItemState.asStateFlow()

    private var productId by mutableLongStateOf(0L)
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf(byteArrayOf())
        private set
    var date by mutableLongStateOf(0L)
        private set
    var dateOfPayment by mutableLongStateOf(0L)
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

    val notifyItem = snapshotFlow { !isPaid }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = true
        )

    val isItemNotEmpty = snapshotFlow {
        quantity.isNotEmpty() && stringToInt(quantity) != 0 && purchasePrice.isNotEmpty()
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

    fun updateDateOfPayment(dateOfPayment: Long) {
        this.dateOfPayment = dateOfPayment
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

    fun insertItems(productId: Long, onError: (error: Int) -> Unit) {
        _stockItemState.value = UiState.InProgress
        viewModelScope.launch {
            stockRepository.insert(
                model = createStockItem(productId = productId),
                onError = {
                    onError(it)
                    _stockItemState.value = UiState.Fail
                },
                onSuccess = { _stockItemState.value = UiState.Success }
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
                dateOfPayment = it.dateOfPayment
                validity = it.validity
                category = setCategories(it.categories)
                company = it.company
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                isPaid = it.isPaid
            }
        }
    }

    fun updateStockItem(stockItemId: Long, onError: (error: Int) -> Unit) {
        _stockItemState.value = UiState.InProgress
        viewModelScope.launch {
            stockRepository.update(
                model = updateStockItem(stockItemId = stockItemId),
                onError = {
                    onError(it)
                    _stockItemState.value = UiState.Fail
                },
                onSuccess = { _stockItemState.value = UiState.Success }
            )
        }
    }

    fun deleteStockItem(stockOrderId: Long, onError: (error: Int) -> Unit) {
        _stockItemState.value = UiState.InProgress
        viewModelScope.launch {
            stockRepository.deleteById(
                id = stockOrderId,
                timestamp = getCurrentTimestamp(),
                onError = {
                    onError(it)
                    _stockItemState.value = UiState.Fail
                },
                onSuccess = { _stockItemState.value = UiState.Success }
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
        dateOfPayment = dateOfPayment,
        validity = validity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isPaid = isPaid,
        timestamp = getCurrentTimestamp()
    )

    private fun updateStockItem(stockItemId: Long) = StockItem(
        id = stockItemId,
        productId = productId,
        name = name,
        photo = photo,
        quantity = stringToInt(quantity),
        date = date,
        dateOfPayment = dateOfPayment,
        validity = validity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isPaid = isPaid,
        timestamp = getCurrentTimestamp()
    )
}