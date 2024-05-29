package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow

interface CustomerData : DataOperations<Customer> {
    suspend fun insert(
        model: Customer,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit,
    ): Long

    suspend fun update(
        model: Customer,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )

    fun search(search: String): Flow<List<Customer>>

    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Customer>>

    fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<Customer>>
}
