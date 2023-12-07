package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestProductRepository : ProductRepository<Product> {
    private val productList = mutableListOf<Product>()

    override suspend fun insert(model: Product): Long {
        productList.add(model)
        return model.id
    }

    override suspend fun update(model: Product) {
        val index = getIndex(id = model.id, list = productList)
        if (isIndexValid(index = index)) productList[index] = model
    }

    override fun search(value: String): Flow<List<Product>> {
        return flowOf(productList).map {
            it.filter { product ->
                product.name == value || product.company == value || product.description == value ||
                product.categories.joinToString(", ") { category ->  category.category } == value
            }
        }
    }

    override fun searchPerCategory(value: String, categoryId: Long): Flow<List<Product>> {
        return flowOf(productList).map {
            it.filter { product ->
                product.id == categoryId && (product.name == value || product.company == value ||
                        product.description == value)
            }
        }
    }

    override fun getByCategory(category: String): Flow<List<Product>> {
        return flowOf(productList).map {
            it.filter { product ->
                product.categories.joinToString(", ") { category ->
                    category.category
                }.contains(category)
            }
        }
    }

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = productList)
        if (isIndexValid(index = index)) productList.removeAt(index)
    }

    override fun getAll(): Flow<List<Product>> = flowOf(productList)

    override fun getById(id: Long): Flow<Product> {
        val index = getIndex(id = id, list = productList)
        return if (isIndexValid(index = index)) flowOf(productList[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Product> = flowOf(productList.last())
}