package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ExtendedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var _currentSale = MutableStateFlow(Sale.emptySale())
    val currentSale = _currentSale.asStateFlow()

    var sheetName by mutableStateOf("")
        private set

    private val _saleList = MutableStateFlow(emptyList<Sale>())
    val saleList = _saleList
        .map {
            it.map { sale ->
                ExtendedItem(
                    id = sale.id,
                    photo = sale.photo,
                    title = sale.customerName,
                    firstSubtitle = sale.name,
                    secondSubtitle = sale.salePrice.toString(),
                    description = sale.dateOfSale.toString(),
                    footer = sale.company
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getCurrentSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.getById(id = saleId).collect {
                _currentSale.value = it
            }
        }
    }

    fun updateSheetName(sheetName: String) {
        this.sheetName = sheetName
    }

    fun getSales() {
        viewModelScope.launch {
            saleRepository.getAll().collect {
                _saleList.value = it
            }
        }
    }

    fun getOrders() {
        viewModelScope.launch {
            saleRepository.getAllOrdersSales(offset = 0, limit = 100).collect {
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

    fun getOrdersByCustomerName(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getOrdersByCustomerName(isOrderedAsc = isOrderedAsc).collect {
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

    fun getOrdersBySalePrice(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            saleRepository.getOrdersBySalePrice(isOrderedAsc = isOrderedAsc).collect {
                _saleList.value = it
            }
        }
    }

    fun exportSalesSheet() {
        viewModelScope.launch {
            if (sheetName.isNotEmpty())
                saleRepository.exportExcelSheet(sheetName = sheetName)
        }
    }
}