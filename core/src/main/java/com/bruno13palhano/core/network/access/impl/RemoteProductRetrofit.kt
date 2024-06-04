package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.RemoteProductData
import com.bruno13palhano.core.network.model.ProductNet
import javax.inject.Inject

internal class RemoteProductRetrofit
    @Inject
    constructor(
        private val apiService: Service,
    ) : RemoteProductData {
        override suspend fun getAll(): List<ProductNet> {
            return try {
                apiService.getAllProducts()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                emptyList()
            }
        }

        override suspend fun insert(data: ProductNet) = apiService.insertProduct(data)

        override suspend fun update(data: ProductNet) = apiService.updateProduct(data)

        override suspend fun delete(id: Long) = apiService.deleteProduct(id)
    }
