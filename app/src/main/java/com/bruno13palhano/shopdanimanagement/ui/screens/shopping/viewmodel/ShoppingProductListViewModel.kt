package com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShoppingProductListViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>
) : ViewModel() {
    val productList = productRepository.getAll()
        .map {
            it.map { product ->
                Stock(
                    id = product.id,
                    name = product.name,
                    photo = product.photo,
                    purchasePrice = 0F,
                    quantity = 0
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}