package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.DeliveryRep
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    @DeliveryRep private val deliveryRepository: DeliveryData<Delivery>
) : ViewModel() {
    private var _deliveries = MutableStateFlow(emptyList<Delivery>())
    val deliveries = _deliveries
        .map {
            it.map { delivery ->
                CommonItem(
                    id = delivery.id,
                    photo = "",
                    title = delivery.customerName,
                    subtitle = delivery.productName,
                    description = delivery.address
                )
            }
        }
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