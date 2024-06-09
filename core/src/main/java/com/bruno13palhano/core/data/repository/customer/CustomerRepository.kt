package com.bruno13palhano.core.data.repository.customer

import com.bruno13palhano.core.data.RepositoryOperations
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface CustomerRepository : RepositoryOperations<Customer>, Syncable {
    fun search(search: String): Flow<List<Customer>>

    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Customer>>

    fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<Customer>>
}