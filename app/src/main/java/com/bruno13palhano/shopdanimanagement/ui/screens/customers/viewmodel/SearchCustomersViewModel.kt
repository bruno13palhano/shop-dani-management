package com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.SearchCacheRep
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCustomersViewModel @Inject constructor(
    @CustomerRep private val customerRepository: CustomerData<Customer>,
    @SearchCacheRep private val searchCacheRepository: SearchCacheData<SearchCache>
) : ViewModel() {
    val searchCache = searchCacheRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _customers = MutableStateFlow(emptyList<Customer>())
    val customers = _customers
        .map {
            it.map { customer ->
                CommonItem(
                    id = customer.id,
                    photo = customer.photo,
                    title = customer.name,
                    subtitle = customer.email,
                    description = customer.address
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun search(search: String) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                customerRepository.search(search = search).collect {
                    _customers.value = it
                }
            }
        }
    }

    fun insertCache(search: String) {
        if (search.trim().isNotEmpty()) {
            viewModelScope.launch {
                searchCacheRepository.insert(SearchCache(search.trim()))
            }
        }
    }
}