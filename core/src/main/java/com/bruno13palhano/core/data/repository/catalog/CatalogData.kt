package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.model.Catalog
import kotlinx.coroutines.flow.Flow

interface CatalogData : DataOperations<Catalog> {
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>>
    fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>>
}