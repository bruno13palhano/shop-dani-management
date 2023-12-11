package com.bruno13palhano.core.data.repository.product

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.ProductCategoriesTableQueries
import cache.ShopDatabaseQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class DefaultProductData @Inject constructor(
    private val productQueries: ShopDatabaseQueries,
    private val productCategoriesQueries: ProductCategoriesTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ProductData {
    override suspend fun insert(
        model: Product,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        var id = 0L
        try {
            productCategoriesQueries.transaction {
                if (model.isNew()) {
                    productQueries.insert(
                        name = model.name,
                        code = model.code,
                        description = model.description,
                        photo = model.photo,
                        date = model.date,
                        company = model.company,
                        timestamp = model.timestamp
                    )
                    productCategoriesQueries.insert(
                        productId = productQueries.getLastId().executeAsOne(),
                        categories = model.categories
                    )
                    id = productQueries.getLastId().executeAsOne()
                    onSuccess(id)
                } else {
                    productQueries.insertWithId(
                        id = model.id,
                        name = model.name,
                        code = model.code,
                        description = model.description,
                        photo = model.photo,
                        date = model.date,
                        company = model.company,
                        timestamp = model.timestamp
                    )
                    try {
                        val categoryId =
                            productCategoriesQueries.getIdByProductId(model.id).executeAsOne()
                        productCategoriesQueries.update(
                            id = categoryId,
                            productId = model.id,
                            categories = model.categories
                        )
                    } catch (e: Exception) {
                        productCategoriesQueries.insert(
                            productId = model.id,
                            categories = model.categories
                        )
                    }
                    id = model.id
                    onSuccess(id)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(1)
        }

        return id
    }

    override suspend fun update(
        model: Product,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            productQueries.transaction {
                val categoryId =
                    productCategoriesQueries.getIdByProductId(productId = model.id).executeAsOne()
                productCategoriesQueries.update(
                    productId = model.id,
                    categories = model.categories,
                    id = categoryId
                )
                productQueries.update(
                    name = model.name,
                    code = model.code,
                    description = model.description,
                    photo = model.photo,
                    date = model.date,
                    company = model.company,
                    id = model.id,
                    timestamp = model.timestamp
                )
                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(2)
        }
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

    override fun searchPerCategory(value: String, categoryId: Long): Flow<List<Product>> {
        return productQueries.searchPerCategory(
            name = value,
            description = value,
            company = value,
            categoryId = categoryId,
            mapper = ::mapProduct
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getByCategory(category: String): Flow<List<Product>> {
        return productQueries.getByCategory(category = category, mapper = ::mapProduct)
            .asFlow().mapToList(ioDispatcher)
    }

    override suspend fun deleteById(
        id: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            productQueries.delete(productId = id)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(3)
        }
    }

    override fun getAll(): Flow<List<Product>> {
        return productQueries.getAll(::mapProduct).asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Product> {
        return productQueries.getById(produtId = id, mapper = ::mapProduct)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Product> {
        return productQueries.getLast(mapper = ::mapProduct)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    private fun mapProduct(
        id: Long,
        name: String,
        code: String,
        description: String,
        photo: ByteArray,
        date: Long,
        categories: List<Category>,
        company: String,
        timestamp: String
    ): Product {
        return Product(
            id = id,
            name = name,
            code = code,
            description = description,
            photo = photo,
            date = date,
            categories = categories,
            company = company,
            timestamp = timestamp
        )
    }
}