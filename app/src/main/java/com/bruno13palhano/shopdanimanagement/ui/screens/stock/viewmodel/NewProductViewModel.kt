package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: DataOperations<Product>,
    @DefaultCategoryRepository private val categoryRepository: DataOperations<Category>
): ViewModel() {
    var name by mutableStateOf("")
        private set
    var code by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var quantity by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
    var date: String by mutableStateOf(SimpleDateFormat.getDateInstance().format(dateInMillis))
        private set
    var validityInMillis by mutableLongStateOf(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
    var validity: String by mutableStateOf(SimpleDateFormat.getDateInstance().format(validityInMillis))
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var salePrice by mutableStateOf("")
        private set
    var isPaid by mutableStateOf(false)
        private set
    var categories by mutableStateOf(listOf(""))
        private set

    val allCategories = categoryRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun updateName(name: String) {
        this.name = name
    }

    fun updateCode(code: String) {
        this.code = code
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updatePhoto(photo: String) {
        this.photo = photo
    }

    fun updateCategory(category: String) {
        this.category = category
    }

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateDate(date: Long) {
        val dateFormat = SimpleDateFormat.getDateInstance()
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateInMillis = date
        this.date = dateFormat.format(dateInMillis)
    }

    fun updateValidity(validity: Long) {
        val dateFormat = SimpleDateFormat.getDateInstance()
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        validityInMillis = validity
        this.validity = dateFormat.format(validityInMillis)
    }

    fun updateCompany(company: String) {
        this.company = company
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

    fun insertProduct() {
        val product = Product(
            id = 0L,
            name = name,
            description = description,
            photo = photo,
            categories = categories,
            company = company,
            purchasePrice = stringToFloat(purchasePrice),
            salePrice = stringToFloat(salePrice),
            isPaid = isPaid,
            isSold = false
        )

        viewModelScope.launch {
            productRepository.insert(product)
        }
    }
}