package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.SearchCacheRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.core.model.StockOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockOrdersSearchViewModel @Inject constructor(
    @StockOrderRep private val stockOrderRepository: StockOrderData<StockOrder>,
    @SearchCacheRep private val searchCacheRepository: SearchCacheData<SearchCache>
) : ViewModel() {
    private val _searchCache = MutableStateFlow<List<SearchCache>>(emptyList())
    val searchCache = _searchCache.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _stockOrderItems = MutableStateFlow(emptyList<Stock>())
    val stockOrderItems = _stockOrderItems.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun search(search: String, isOrderedByCustomer: Boolean) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                stockOrderRepository.search(search, isOrderedByCustomer).collect {
                    _stockOrderItems.value = it.map { item ->
                        Stock(
                            id = item.id,
                            name = item.name,
                            photo = item.photo,
                            purchasePrice = item.purchasePrice,
                            quantity = item.quantity
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