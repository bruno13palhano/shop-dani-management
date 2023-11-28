package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.network.Service
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

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteCatalogItem(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: Catalog) {
        try {
            apiService.updateCatalogItem(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun insert(data: Catalog) {
        try {
            apiService.insertCatalogItem(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}