package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.model.CatalogNet
import javax.inject.Inject

internal class CatalogNetworkRetrofit
    @Inject
    constructor(
        private val apiService: Service,
    ) : CatalogNetwork {
        override suspend fun getAll(): List<CatalogNet> {
            return try {
                apiService.getCatalog()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                emptyList()
            }
        }

        override suspend fun delete(id: Long) = apiService.deleteCatalogItem(id)

        override suspend fun update(data: CatalogNet) = apiService.updateCatalogItem(data)

        override suspend fun insert(data: CatalogNet) = apiService.insertCatalogItem(data)
    }
