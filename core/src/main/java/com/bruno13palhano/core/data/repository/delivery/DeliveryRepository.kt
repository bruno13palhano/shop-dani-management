package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.RepositoryOperations
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository : RepositoryOperations<Delivery>, Syncable {
    fun getDeliveries(delivered: Boolean): Flow<List<Delivery>>
    fun getCanceledDeliveries(): Flow<List<Delivery>>
}