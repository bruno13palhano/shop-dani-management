package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerData : DataOperations<Customer> {
    fun search(search: String): Flow<List<Customer>>
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Customer>>
    fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<Customer>>
}