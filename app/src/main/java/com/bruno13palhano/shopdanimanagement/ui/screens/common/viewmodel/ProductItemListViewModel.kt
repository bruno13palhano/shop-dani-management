package com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductItemListViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
) : ViewModel() {
    private val _productList = productRepository.getAll()
    val productList = _productList
        .map {
            it.map { product ->
                CommonItem(
                    id = product.id,
                    photo = product.photo,
                    title = product.name,
                    subtitle = product.company,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}