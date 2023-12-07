package com.bruno13palhano.core.data.repository.product

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.ProductCategoriesTableQueries
import cache.ShopDatabaseQueries
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.isNew
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class ProductLight @Inject constructor(
    private val productQueries: ShopDatabaseQueries,
    private val productCategoriesQueries: ProductCategoriesTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ProductData<Product> {
    override suspend fun insert(model: Product): Long {
        productCategoriesQueries.transaction {
            if (model.isNew()) {
                productQueries.insert(
                    name = model.name,
                    code = model.code,
                    description = model.description,
                    photo = model.photo,
                    date = model.date,
                    company = model.company,
                    timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .format(model.timestamp)
                )
                productCategoriesQueries.insert(
                    productId = productQueries.getLastId().executeAsOne(),
                    categories = model.categories
                )
            } else {
                productQueries.insertWithId(
                    id = model.id,
                    name = model.name,
                    code = model.code,
                    description = model.description,
                    photo = model.photo,
                    date = model.date,
                    company = model.company,
                    timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .format(model.timestamp)
                )
                try {
                    val categoryId = productCategoriesQueries.getIdByProductId(model.id).executeAsOne()
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
            }
        }
        return productQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Product) {
        val categoryId = productQueries.getCategoryId(id = model.id).executeAsOne()
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
            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(model.timestamp)
        )
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

    override suspend fun deleteById(id: Long) {
        productQueries.delete(productId = id)
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

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
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
            timestamp = OffsetDateTime.parse(timestamp)
        )
    }
}