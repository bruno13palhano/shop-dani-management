package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.DefaultDeliveryRepository
import com.bruno13palhano.core.model.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    @DefaultDeliveryRepository private val deliveryRepository: DeliveryData<Delivery>
) : ViewModel() {

}