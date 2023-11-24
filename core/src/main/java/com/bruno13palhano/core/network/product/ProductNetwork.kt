package com.bruno13palhano.core.network.product

import com.bruno13palhano.core.model.Product

internal interface ProductNetwork {
    suspend fun getAll(): List<Product>
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(id: Long)
}