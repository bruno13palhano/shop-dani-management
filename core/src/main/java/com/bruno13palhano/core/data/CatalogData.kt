package com.bruno13palhano.core.data

import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface CatalogData<T> : DataOperations<T>, Syncable {
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<T>>
    fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<T>>
}