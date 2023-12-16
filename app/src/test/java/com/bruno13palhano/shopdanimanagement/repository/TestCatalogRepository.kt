package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class TestCatalogRepository : CatalogRepository {
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

    override suspend fun insert(
        model: Catalog,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        catalogList.add(model)
        onSuccess(model.id)
        return model.id
    }

    override suspend fun update(
        model: Catalog,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = model.id, list = catalogList)
        if (isIndexValid(index = index)) {
            catalogList[index] = model
            onSuccess()
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = id, list = catalogList)
        if (isIndexValid(index = index)) {
            catalogList.removeAt(index)
            onSuccess()
        }
    }

    override fun getAll(): Flow<List<Catalog>> = flowOf(catalogList)

    override fun getById(id: Long): Flow<Catalog> {
        val index = getIndex(id = id, list = catalogList)
        return if (isIndexValid(index = index)) flowOf(catalogList[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Catalog> = flowOf(catalogList.last())

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}