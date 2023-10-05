package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface CatalogData<T> : DataOperations<T> {
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<T>>
}