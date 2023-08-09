package com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.data.di.DefaultSearchCacheRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.SearchCache
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
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultSearchCacheRepository private val searchCacheRepository: SearchCacheData<SearchCache>
) : ViewModel() {
    private val _searchCache = MutableStateFlow<List<SearchCache>>(emptyList())
    val searchCache = _searchCache.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _stockProducts = MutableStateFlow(emptyList<Stock>())
    val stockProducts = _stockProducts.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = emptyList()
        )

    fun search(search: String, isOrderedByCostumer: Boolean) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                productRepository.searchProduct(search.trim(), isOrderedByCostumer).collect {
                    _stockProducts.value = it.map { product ->
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

    fun getSearchCache(isOrderedByCustomer: Boolean) {
        viewModelScope.launch {
            searchCacheRepository.getSearchCache(isOrderedByCustomer).collect {
                _searchCache.value = it
            }
        }
    }

    fun insertSearch(search: String, isOrderedByCustomer: Boolean) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                searchCacheRepository.insert(SearchCache(search.trim(), isOrderedByCustomer))
            }
        }
    }
}