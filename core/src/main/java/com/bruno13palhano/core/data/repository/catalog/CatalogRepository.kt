package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.di.InternalCatalogLight
import com.bruno13palhano.core.model.Catalog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    @InternalCatalogLight private val catalogData: CatalogData<Catalog>
) : CatalogData<Catalog> {
    override suspend fun insert(model: Catalog): Long {
        return catalogData.insert(model = model)
    }

    override suspend fun update(model: Catalog) {
        catalogData.update(model = model)
    }

    override suspend fun deleteById(id: Long) {
        catalogData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Catalog>> {
        return catalogData.getAll()
    }

    override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>> {
        return catalogData.getOrderedByName(isOrderedAsc = isOrderedAsc)
    }

    override fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>> {
        return catalogData.getOrderedByPrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getById(id: Long): Flow<Catalog> {
        return catalogData.getById(id = id)
    }

    override fun getLast(): Flow<Catalog> {
        return catalogData.getLast()
    }
}