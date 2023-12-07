package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.data.di.DeliveryRep
import com.bruno13palhano.core.model.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    @DeliveryRep private val deliveryRepository: DeliveryRepository
) : ViewModel() {
    private var _deliveries = MutableStateFlow(emptyList<Delivery>())
    val deliveries = _deliveries
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getAllDeliveries() {
        viewModelScope.launch {
            deliveryRepository.getAll().collect {
                _deliveries.value = it
            }
        }
    }

    fun getDeliveries(delivered: Boolean) {
        viewModelScope.launch {
            deliveryRepository.getDeliveries(delivered).collect {
                _deliveries.value = it
            }
        }
    }
}