package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CanceledSalesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>
) : ViewModel() {
    private val _canceledSale = MutableStateFlow(emptyList<Sale>())
    val canceledSales = _canceledSale
        .map {
            it.map { sale ->
                CommonItem(
                    id = sale.id,
                    photo = sale.photo,
                    title = sale.name,
                    subtitle = sale.salePrice.toString(),
                    description = sale.customerName
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getAllCanceledSales() {
        viewModelScope.launch {
            saleRepository.getAllCanceledSales().collect {
                _canceledSale.value = it
            }
        }
    }

    fun getCanceledSalesByName(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getCanceledByName(isOrderedAsc = isOrderedAsc).collect {
                _canceledSale.value = it
            }
        }
    }

    fun getCanceledSalesByCustomerName(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getCanceledByCustomerName(isOrderedAsc = isOrderedAsc).collect {
                _canceledSale.value = it
            }
        }
    }

    fun getCanceledSalesByPrice(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getCanceledByPrice(isOrderedAsc = isOrderedAsc).collect {
                _canceledSale.value = it
            }
        }
    }
}