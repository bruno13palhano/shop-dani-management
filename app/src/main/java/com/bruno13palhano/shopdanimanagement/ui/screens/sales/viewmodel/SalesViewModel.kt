package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.SalesInformation
import com.bruno13palhano.core.data.domain.SaleInfoUseCase
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SaleInfo
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
    @SaleRep private val saleRepository: SaleRepository,
    @SalesInformation private val salesInfoUseCase: SaleInfoUseCase
) : ViewModel() {
    private var _currentSaleInfo = MutableStateFlow(SaleInfo.emptySaleInfo())
    val currentSale = _currentSaleInfo.asStateFlow()

    var sheetName by mutableStateOf("")
        private set

    private val _saleList = MutableStateFlow(emptyList<Sale>())
    val saleList = _saleList
        .map {
            it.map { sale ->
                SaleInfo(
                    saleId = sale.id,
                    productId = sale.productId,
                    customerId = sale.customerId,
                    productName = sale.name,
                    customerName = sale.customerName,
                    productPhoto = sale.photo,
                    customerPhoto = byteArrayOf(),
                    address = sale.address,
                    phoneNumber = sale.phoneNumber,
                    email = "",
                    salePrice = sale.salePrice,
                    deliveryPrice = sale.deliveryPrice,
                    quantity = sale.quantity,
                    dateOfSale = sale.dateOfSale
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getCurrentSale(saleId: Long, customerId: Long) {
        viewModelScope.launch {
            salesInfoUseCase(saleId = saleId, customerId = customerId).collect {
                _currentSaleInfo.value = it
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