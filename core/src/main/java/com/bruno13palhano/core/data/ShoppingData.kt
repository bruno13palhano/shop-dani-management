package com.bruno13palhano.core.data

import kotlinx.coroutines.flow.Flow

interface ShoppingData<T> : DataOperations<T> {
    fun getItemsLimited(offset: Int, limit: Int): Flow<List<T>>
}