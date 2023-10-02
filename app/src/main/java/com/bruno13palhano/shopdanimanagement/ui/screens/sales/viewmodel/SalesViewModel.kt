package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val _saleList = MutableStateFlow(emptyList<Sale>())
    val saleList = _saleList
        .map {
            it.map { sale ->
                CommonItem(
                    id = sale.id,
                    photo = sale.photo,
                    title = sale.name,
                    subtitle = sale.company,
                    description = dateFormat.format(sale.dateOfSale)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getSales() {
        viewModelScope.launch {
            saleRepository.getAll().collect {
                _saleList.value = it
            }
        }
    }

    fun getSalesByCustomerName(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getAllSalesByCustomerName(isOrderedAsc = isOrderedAsc).collect {
                _saleList.value = it
            }
        }
    }

    fun getSalesBySalePrice(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getAllSalesBySalePrice(isOrderedAsc = isOrderedAsc).collect {
                _saleList.value = it
            }
        }
    }
}