package com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.SearchCacheRep
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductsViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductData<Product>,
    @SearchCacheRep private val searchCacheRepository: SearchCacheData<SearchCache>
) : ViewModel() {
    private val _searchCache = MutableStateFlow<List<SearchCache>>(emptyList())
    val searchCache = _searchCache.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _products = MutableStateFlow(emptyList<CommonItem>())
    val products = _products.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = emptyList()
        )

    fun search(search: String) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                productRepository.search(search).collect {
                    _products.value = it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name,
                            subtitle = product.company,
                            description = product.description,
                            photo = product.photo,
                        )
                    }
                }
            }
        }
    }

    fun searchPerCategory(search: String, categoryId: Long) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                productRepository.searchPerCategory(
                    value = search,
                    categoryId = categoryId
                ).collect {
                    _products.value = it.map { product ->
                        CommonItem(
                            id = product.id,
                            title = product.name,
                            subtitle = product.company,
                            description = product.description,
                            photo = product.photo
                        )
                    }
                }
            }
        }
    }

    fun getSearchCache() {
        viewModelScope.launch {
            searchCacheRepository.getAll().collect {
                _searchCache.value = it
            }
        }
    }

    fun insertSearch(search: String) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                searchCacheRepository.insert(SearchCache(search.trim()))
            }
        }
    }
}