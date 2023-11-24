package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.CrudNetwork
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class ProductNetworkRetrofit @Inject constructor(
    private val apiService: Service
): CrudNetwork<Product> {
    override suspend fun getAll(): List<Product> {
        return try {
            apiService.getAllProducts().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun insert(data: Product) {
        try {
            apiService.insertProduct(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: Product) {
        try {
            apiService.updateProduct(data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteProduct(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}