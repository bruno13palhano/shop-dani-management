package com.bruno13palhano.core.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.ProductCategoriesTableQueries
import cache.ShopDatabaseQueries
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ProductRepositoryLight @Inject constructor(
    private val productQueries: ShopDatabaseQueries,
    private val productCategoriesQueries: ProductCategoriesTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ProductData<Product> {
    override suspend fun insert(model: Product): Long {
        productCategoriesQueries.insert(
            categories = model.categories
        )
        productQueries.insert(
            name = model.name,
            code = model.code,
            description = model.description,
            photo = model.photo,
            date = model.date,
            categoryId = productCategoriesQueries.getLastId().executeAsOne(),
            company = model.company
        )
        return productQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Product) {
        val categoryId = productQueries.getCategoryId(id = model.id).executeAsOne()
        productCategoriesQueries.update(
            categories = model.categories,
            id = categoryId
        )
        productQueries.update(
            name = model.name,
            code = model.code,
            description = model.description,
            photo = model.photo,
            date = model.date,
            categoryId = categoryId,
            company = model.company,
            id = model.id
        )
    }

    override suspend fun delete(model: Product) {
        productQueries.delete(productId = model.id)
    }

    override fun search(value: String): Flow<List<Product>> {
        return productQueries.search(
            category = value,
            name = value,
            description = value,
            company = value,
            mapper = ::mapProduct
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getByCategory(category: String): Flow<List<Product>> {
        return productQueries.getByCategory(category = category, mapper = ::mapProduct)
            .asFlow().mapToList(ioDispatcher)
    }

    override suspend fun deleteById(id: Long) {
        productQueries.delete(productId = id)
    }

    override fun getAll(): Flow<List<Product>> {
        return productQueries.getAll(::mapProduct).asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Product> {
        return productQueries.getById(produtId = id, mapper = ::mapProduct)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun getLast(): Flow<Product> {
        return productQueries.getLast(mapper = ::mapProduct)
            .asFlow().mapToOne(ioDispatcher)
    }

    private fun mapProduct(
        id: Long,
        name: String,
        code: String,
        description: String,
        photo: String,
        date: Long,
        categories: List<String>?,
        company: String
    ): Product {
        return Product(
            id = id,
            name = name,
            code = code,
            description = description,
            photo = photo,
            date = date,
            categories = categories!!,
            company = company
        )
    }
}