package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.model.Sale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StockOrdersSalesViewModel @Inject constructor(
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>
) : ViewModel() {

}