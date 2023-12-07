package com.bruno13palhano.core.data

import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface CustomerData<T> : DataOperations<T>, Syncable {
    fun search(search: String): Flow<List<T>>
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<T>>
}