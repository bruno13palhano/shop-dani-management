package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.SearchCacheRep
import com.bruno13palhano.core.data.di.StockRep
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockSearchViewModel
    @Inject
    constructor(
        @StockRep private val stockRepository: StockRepository,
        @SearchCacheRep private val searchCacheRepository: SearchCacheRepository
    ) : ViewModel() {
        private val _searchCache = MutableStateFlow<List<SearchCache>>(emptyList())
        val searchCache =
            _searchCache.asStateFlow()
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

        private val _stockOrderItems = MutableStateFlow(emptyList<Stock>())
        val stockOrderItems =
            _stockOrderItems.asStateFlow()
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

        fun search(search: String) {
            if (search.trim().isNotEmpty()) {
                viewModelScope.launch {
                    stockRepository.search(search).collect {
                        _stockOrderItems.value =
                            it.map { item ->
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