package com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel

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
import com.bruno13palhano.core.model.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>,
    @DefaultProductRepository private val productRepository: ProductData<Product>
): ViewModel() {
    private val _products = MutableStateFlow<List<Stock>>(emptyList())
    val products = _products.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var categories = categoryRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var newName by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        this.newName = newName
    }

    fun insertCategory() {
        val category = Category(0L, newName.trim())
        viewModelScope.launch {
            categoryRepository.insert(category)
        }
        restoreValue()
    }

    fun getAllProducts() {
        viewModelScope.launch {

        }
    }

    private fun restoreValue() {
        newName = ""
    }
}