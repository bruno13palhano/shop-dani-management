package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.InternalProductLight
import com.bruno13palhano.core.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProductRepository @Inject constructor(
    @InternalProductLight private val productData: InternalProductData
): ProductData<Product> {
    override suspend fun insert(model: Product): Long {
        return productData.insert(model = model)
    }

    override suspend fun update(model: Product) {
        productData.update(model = model)
    }

    override suspend fun delete(model: Product) {
        productData.delete(model = model)
    }

    override fun search(value: String): Flow<List<Product>> {
        return productData.search(value = value)
    }

    override fun getByCategory(category: String): Flow<List<Product>> {
        return productData.getByCategory(category = category)
    }

    override suspend fun deleteById(id: Long) {
        return productData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Product>> {
        return productData.getAll()
    }

    override fun getById(id: Long): Flow<Product> {
        return productData.getById(id = id)
    }

    override fun getLast(): Flow<Product> {
        return productData.getLast()
    }
}