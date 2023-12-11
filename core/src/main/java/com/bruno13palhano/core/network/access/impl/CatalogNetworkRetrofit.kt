package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class CatalogNetworkRetrofit @Inject constructor(
    private val apiService: Service
): CatalogNetwork {
    override suspend fun getAll(): List<Catalog> {
        return try {
            apiService.getCatalog().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun delete(id: Long) = apiService.deleteCatalogItem(id)

    override suspend fun update(data: Catalog) =
        apiService.updateCatalogItem(data.asNetwork())

    override suspend fun insert(data: Catalog) =
        apiService.insertCatalogItem(data.asNetwork())
}