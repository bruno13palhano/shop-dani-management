package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.DefaultProductRepository
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
class SearchProductsViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>
) : ViewModel() {
    private val _stock = MutableStateFlow(emptyList<Stock>())
    val stock = _stock.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = emptyList()
        )

    fun search(search: String) {
        viewModelScope.launch {
            productRepository.search(search).collect {
                _stock.value = it.map { product ->
                    Stock(
                        id = product.id,
                        name = product.name,
                        photo = product.photo,
                        purchasePrice = product.purchasePrice,
                        quantity = product.quantity
                    )
                }
            }
        }
    }
}