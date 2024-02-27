package com.bruno13palhano.shopdanimanagement.ui.screens.amazon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.SearchCacheRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAmazonViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository,
    @SearchCacheRep private val searchCacheRepository: SearchCacheRepository
) : ViewModel() {
    val searchCache = searchCacheRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _amazonSales = MutableStateFlow(emptyList<CommonItem>())
    val amazonSales = _amazonSales
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun search(search: String) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                searchCacheRepository.insert(SearchCache(search = search.trim()))
            }

            viewModelScope.launch {
                saleRepository.searchAmazonSales(search = search).collect {
                    _amazonSales.value = it.map { sale ->
                        CommonItem(
                            id = sale.id,
                            photo = sale.photo,
                            title = sale.name,
                            subtitle = sale.company,
                            description = sale.customerName
                        )
                    }
                }
            }
        }
    }
}