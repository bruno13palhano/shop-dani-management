package com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>,
    @DefaultProductRepository private val productRepository: ProductData<Product>
) : ViewModel() {
    val categories = categoryRepository.getAll()
        .map {
            it.map { category -> category.name }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _orders = MutableStateFlow<List<CommonItem>>(emptyList())
    val orders = _orders.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var name by mutableStateOf("")
        private set

    fun updateName(name: String) {
        this.name = name
    }

    fun updateCategory(id: Long) {
        val category = Category(id, name.trim())
        viewModelScope.launch {
            categoryRepository.update(category)
        }
    }

    fun getCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.getById(id).collect {
                name = it.name
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            productRepository.getAll().collect {
                _orders.value = it.map { product ->
                    CommonItem(
                        id = product.id,
                        photo = product.photo,
                        title = product.name,
                        subtitle = product.company,
                        description = dateFormat.format(product.date)
                    )
                }
            }
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            productRepository.getByCategory(category).collect {
                _orders.value = it.map { product ->
                    CommonItem(
                        id = product.id,
                        photo = product.photo,
                        title = product.name,
                        subtitle = product.company,
                        description = dateFormat.format(product.date)
                    )
                }
            }
        }
    }
}