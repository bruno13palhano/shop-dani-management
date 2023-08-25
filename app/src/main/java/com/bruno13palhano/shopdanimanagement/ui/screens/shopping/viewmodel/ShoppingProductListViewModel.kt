package com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingProductListViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>
) : ViewModel() {
    val categories = categoryRepository.getAll()
        .map {
            it.map { category -> category.name}
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    private val _productList = MutableStateFlow(emptyList<CommonItem>())
    val productList = _productList
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getAllProducts() {
        viewModelScope.launch {
            productRepository.getAll().collect {
                _productList.value = it.map { product ->
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

    fun getProductByCategory(category: String) {
        viewModelScope.launch {
            productRepository.getByCategory(category).collect {
                _productList.value = it.map { product ->
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