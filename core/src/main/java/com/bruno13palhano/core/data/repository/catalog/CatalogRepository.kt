package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.RepositoryOperations
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface CatalogRepository : RepositoryOperations<Catalog>, Syncable {
    fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>>

    fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>>
}