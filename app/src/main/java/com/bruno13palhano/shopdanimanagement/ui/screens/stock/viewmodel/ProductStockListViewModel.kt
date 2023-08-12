package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductStockListViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>
) : ViewModel() {
    private val _categoryName = MutableStateFlow("")
    val categoryName = _categoryName.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList = _productList
        .map {
            it.map { product ->
                Stock(
                    id = product.id,
                    name = product.name,
                    photo = product.photo,
                    purchasePrice = product.purchasePrice,
                    quantity = 0,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getCategoryName(categoryId: Long) {
        viewModelScope.launch {
            categoryRepository.getById(categoryId).collect {
                _categoryName.value = it.name
            }
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            productRepository.getByCategory(category).collect {
                _productList.value = it
            }
        }
    }
}