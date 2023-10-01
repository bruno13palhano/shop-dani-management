package com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel

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
class CustomersDebitViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleData<Sale>,
) : ViewModel() {
    private val _debits = MutableStateFlow(emptyList<Sale>())
    val debits = _debits
        .map {
            it.map { sale ->
                CommonItem(
                    id = sale.id,
                    photo = sale.photo,
                    title = sale.customerName,
                    subtitle = sale.salePrice.toString(),
                    description = dateFormat.format(sale.dateOfPayment)
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getDebitByCustomerNameDesc() {
        viewModelScope.launch {
            saleRepository.getDebitSalesByCustomerNameDesc().collect {
                _debits.value = it
            }
        }
    }

    fun getDebitByCustomerNameAsc() {
        viewModelScope.launch {
            saleRepository.getDebitSalesByCustomerNameAsc().collect {
                _debits.value = it
            }
        }
    }
}