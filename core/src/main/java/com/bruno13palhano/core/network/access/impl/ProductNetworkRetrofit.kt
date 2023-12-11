package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class ProductNetworkRetrofit @Inject constructor(
    private val apiService: Service
): ProductNetwork {
    override suspend fun getAll(): List<Product> {
        return try {
            apiService.getAllProducts().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun insert(data: Product) =
        apiService.insertProduct(data.asNetwork())

    override suspend fun update(data: Product) =
        apiService.updateProduct(data.asNetwork())

    override suspend fun delete(id: Long) = apiService.deleteProduct(id)
}