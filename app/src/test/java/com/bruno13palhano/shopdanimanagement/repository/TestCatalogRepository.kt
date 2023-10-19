package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.model.Catalog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class TestCatalogRepository : CatalogData<Catalog> {
    private val catalogList = mutableListOf<Catalog>()

    override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>> {
        return if (isOrderedAsc) {
            flowOf(catalogList.sortedBy { it.name })
        } else {
            flowOf(catalogList.sortedByDescending { it.name })
        }
    }

    override fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>> {
        return if (isOrderedAsc) {
            flowOf(catalogList.sortedBy { it.price })
        } else {
            flowOf(catalogList.sortedByDescending { it.price })
        }
    }

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = catalogList)
        if (isIndexValid(index = index)) catalogList.removeAt(index)
    }

    override fun getAll(): Flow<List<Catalog>> = flowOf(catalogList)

    override fun getById(id: Long): Flow<Catalog> {
        val index = getIndex(id = id, list = catalogList)
        return if (isIndexValid(index = index)) flowOf(catalogList[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Catalog> = flowOf(catalogList.last())

    override suspend fun update(model: Catalog) {
        val index = getIndex(id = model.id, list = catalogList)
        if (isIndexValid(index = index)) catalogList[index] = model
    }

    override suspend fun insert(model: Catalog): Long {
        catalogList.add(model)
        return model.id
    }
}