package com.bruno13palhano.core.network.product

import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.Service
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

    override suspend fun insertProduct(product: Product) {
        try {
            apiService.insertProduct(product.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun updateProduct(product: Product) {
        try {
            apiService.updateProduct(product.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun deleteProduct(id: Long) {
        try {
            apiService.deleteProduct(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}