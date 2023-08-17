package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Sale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {
    val saleList = saleRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}