package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryData : DataOperations<Delivery> {
    fun getDeliveries(delivered: Boolean): Flow<List<Delivery>>
    fun getCanceledDeliveries(): Flow<List<Delivery>>
}