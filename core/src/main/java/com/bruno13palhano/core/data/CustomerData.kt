package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface CustomerData<T> : DataOperations<T> {
    fun search(search: String): Flow<List<T>>
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<T>>
}