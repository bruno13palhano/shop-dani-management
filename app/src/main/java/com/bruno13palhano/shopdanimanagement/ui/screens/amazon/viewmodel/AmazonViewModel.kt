package com.bruno13palhano.shopdanimanagement.ui.screens.amazon.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.SaleInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmazonViewModel
    @Inject
    constructor(
        @SaleRep private val saleRepository: SaleRepository
    ) : ViewModel() {
        var sheetName by mutableStateOf("")
            private set

        val amazonSale =
            saleRepository.getAmazonSale()
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

        fun updateSheetName(sheetName: String) {
            this.sheetName = sheetName
        }

        fun createSpreadsheet() {
            if (sheetName.isNotEmpty()) {
                viewModelScope.launch {
                    saleRepository.exportAmazonExcelSheet(sheetName = sheetName)
                }
            }
        }
    }