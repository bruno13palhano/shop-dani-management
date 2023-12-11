package com.bruno13palhano.shopdanimanagement.ui.screens.catalog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.di.CatalogRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogItemViewModel @Inject constructor(
    @CatalogRep private val catalogRepository: CatalogRepository,
    @ProductRep private val productRepository: ProductRepository
) : ViewModel() {
    private var productId = 0L
    private var catalogId = 0L
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf(byteArrayOf())
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var discount by mutableStateOf("")
        private set
    var price by mutableStateOf("")
        private set

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updatePrice(price: String) {
        this.price = price
    }

    fun updateDiscount(discount: String) {
        this.discount = discount
    }

    fun getProduct(id: Long) {
        productId = id
        viewModelScope.launch {
            productRepository.getById(id).collect {
                name = it.name
                photo = it.photo
            }
        }
    }

    fun getCatalogItem(id: Long) {
        viewModelScope.launch {
            catalogRepository.getById(id).collect {
                catalogId = it.id
                productId = it.productId
                name = it.name
                photo = it.photo
                title = it.title
                description = it.description
                discount = it.discount.toString()
                price = it.price.toString()
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            catalogRepository.insert(
                model = initCatalogItem(),
                onError = {},
                onSuccess = {}
            )
        }
    }

    fun update() {
        viewModelScope.launch {
            catalogRepository.update(
                model = initCatalogItem(),
                onError = {},
                onSuccess = {}
            )
        }
    }

    fun delete() {
        viewModelScope.launch {
            catalogRepository.deleteById(
                id = catalogId,
                timestamp = getCurrentTimestamp(),
                onError = {},
                onSuccess = {}
            )
        }
    }

    private fun initCatalogItem() = Catalog(
        id = catalogId,
        productId = productId,
        name = name,
        photo = photo,
        title = title,
        description = description,
        discount = stringToLong(discount),
        price = stringToFloat(price),
        timestamp = getCurrentTimestamp()
    )

    private fun stringToLong(value: String) = try { value.toLong() } catch (ignored: Exception) { 0L }
    private fun stringToFloat(value: String): Float {
        return try {
            value.replace(",", ".").toFloat()
        } catch (ignored: Exception) { 0F }
    }
}