package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.DefaultDeliveryRepository
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    @DefaultDeliveryRepository private val deliveryRepository: DeliveryData<Delivery>
) : ViewModel() {
    val deliveries = deliveryRepository.getAll()
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
}