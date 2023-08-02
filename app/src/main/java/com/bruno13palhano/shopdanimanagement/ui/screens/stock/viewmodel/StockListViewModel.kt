package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    @DefaultCategoryRepository private val categoryRepository: DataOperations<Category>,
    @DefaultProductRepository private val productRepository: DataOperations<Product>
): ViewModel() {
    val stock = productRepository.getAll()
        .map {
            it.map { product ->
                Stock(
                    id = product.id,
                    name = product.name,
                    photo = product.photo,
                    purchasePrice = product.purchasePrice,
                    quantity = product.quantity
                )
            }
        }
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
        val category = Category(id, name)
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
}