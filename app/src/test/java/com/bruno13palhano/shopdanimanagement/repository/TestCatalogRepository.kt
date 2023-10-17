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
        var index = -1

        for (i in 0 until catalogList.size) {
            if (catalogList[i].id == id)
                index = i
        }

        if (index != -1)
            catalogList.removeAt(index)
    }

    override fun getAll(): Flow<List<Catalog>> {
        return flowOf(catalogList)
    }

    override fun getById(id: Long): Flow<Catalog> {
        var index = -1

        for (i in 0 until catalogList.size) {
            if (catalogList[i].id == id)
                index = i
        }

        return if (index != -1) flowOf(catalogList[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Catalog> {
        return flowOf(catalogList.last())
    }

    override suspend fun update(model: Catalog) {
        var index = -1

        for (i in 0 until catalogList.size) {
            if (catalogList[i].id == model.id)
                index = i
        }

        if (index != -1)
            catalogList[index] = model
    }

    override suspend fun insert(model: Catalog): Long {
        catalogList.add(model)
        return model.id
    }
}