package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.DataVersion
import kotlinx.coroutines.flow.Flow

interface CatalogData : DataOperations<Catalog> {
    suspend fun insert(
        model: Catalog,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long

    suspend fun update(
        model: Catalog,
        version: DataVersion,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    )

    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>>

    fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>>
}