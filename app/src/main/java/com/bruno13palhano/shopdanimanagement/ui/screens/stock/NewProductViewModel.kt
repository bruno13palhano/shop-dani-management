package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: DataOperations<Product>
): ViewModel() {
    var name by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
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

    fun updateName(name: String) {
        this.name = name
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

    fun updateCompany(company: String) {
        this.company = company
    }

    fun updatePurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun updateSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun updateCategories(categories: List<String>) {
        this.categories = categories
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