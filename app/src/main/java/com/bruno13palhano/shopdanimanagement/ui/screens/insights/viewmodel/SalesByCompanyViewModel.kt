package com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.shopdanimanagement.ui.screens.setChartEntries
import com.bruno13palhano.shopdanimanagement.ui.screens.setQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesByCompanyViewModel @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) : ViewModel() {
    private var days = arrayOf(0)

    private val _chartEntry = MutableStateFlow(SalesCompanyEntries())
    val chartEntry = _chartEntry
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = SalesCompanyEntries()
        )

    fun getChartByRange(rangeOfDays: Int) {
        viewModelScope.launch {
            saleRepository.getAll().collect {
                val avonEntries = mutableListOf<Pair<String, Float>>()
                val naturaEntries = mutableListOf<Pair<String, Float>>()
                val boticarioEntries = mutableListOf<Pair<String, Float>>()
                val eudoraEntries = mutableListOf<Pair<String, Float>>()
                val bereniceEntries = mutableListOf<Pair<String, Float>>()
                val ouiEntries = mutableListOf<Pair<String, Float>>()

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.AVON.company }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(avonEntries, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.NATURA.company }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(naturaEntries, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.BOTICARIO.company }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(boticarioEntries, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.EUDORA.company }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(eudoraEntries, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.BERENICE.company }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(bereniceEntries, days)

                days = Array(rangeOfDays) { 0 }
                it.filter { sale -> sale.company == Company.OUI.company }
                    .map { sale -> setQuantity(days, sale.dateOfSale, sale.quantity) }
                setChartEntries(ouiEntries, days)

                _chartEntry.value = SalesCompanyEntries(
                    avonEntries = avonEntries,
                    naturaEntries = naturaEntries,
                    boticarioEntries = boticarioEntries
                )
            }
        }
    }

    data class SalesCompanyEntries(
        val avonEntries: List<Pair<String, Float>> = listOf(),
        val naturaEntries: List<Pair<String, Float>> = listOf(),
        val boticarioEntries: List<Pair<String, Float>> = listOf(),
        val eudoraEntries: List<Pair<String, Float>> = listOf(),
        val bereniceEntries: List<Pair<String, Float>> = listOf(),
        val ouiEntries: List<Pair<String, Float>> = listOf()
    )
}