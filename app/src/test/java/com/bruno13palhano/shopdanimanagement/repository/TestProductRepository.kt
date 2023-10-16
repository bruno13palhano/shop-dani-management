package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestProductRepository : ProductData<Product> {
    private val productList = mutableListOf<Product>()

    override suspend fun insert(model: Product): Long {
        productList.add(model)
        return model.id
    }

    override suspend fun update(model: Product) {
        var index = -1

        for (i in 0 until productList.size) {
            if (productList[i].id == model.id)
                index = i
        }

        if (index != -1)
            productList[index] = model
    }

    override fun search(value: String): Flow<List<Product>> {
        return flowOf(productList).map {
            it.filter { product ->
                product.name == value || product.company == value || product.description == value ||
                product.categories.joinToString(", ") { category ->  category.name } == value
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
                    category.name
                }.contains(category)
            }
        }
    }

    override suspend fun deleteById(id: Long) {
        var index = -1

        for (i in 0 until productList.size) {
            if (productList[i].id == id)
                index = i
        }

        if (index != -1)
            productList.removeAt(index)
    }

    override fun getAll(): Flow<List<Product>> {
        return flowOf(productList)
    }

    override fun getById(id: Long): Flow<Product> {
        var index = -1

        for (i in 0 until productList.size) {
            if (productList[i].id == id)
                index = i
        }

        return if (index != -1) flowOf(productList[index]) else emptyFlow()
    }

    override fun getLast(): Flow<Product> {
        return flowOf(productList.last())
    }
}