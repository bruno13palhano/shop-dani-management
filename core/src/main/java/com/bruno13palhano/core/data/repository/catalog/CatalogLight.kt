package com.bruno13palhano.core.data.repository.catalog

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CatalogTableQueries
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Catalog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class CatalogLight @Inject constructor(
    private val catalogQueries: CatalogTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CatalogData<Catalog> {
    override suspend fun insert(model: Catalog): Long {
        catalogQueries.insert(
            productId = model.productId,
            title = model.title,
            description = model.description,
            discount = model.discount,
            price = model.price.toDouble()
        )

        return catalogQueries.getLastId().executeAsOne();
    }

    override suspend fun update(model: Catalog) {
        catalogQueries.update(
            title = model.title,
            description = model.description,
            discount = model.discount,
            price = model.price.toDouble(),
            id = model.id
        )
    }

    override suspend fun delete(model: Catalog) {
        catalogQueries.delete(id = model.id)
    }

    override suspend fun deleteById(id: Long) {
        catalogQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<Catalog>> {
        return catalogQueries.getAll(mapper = ::mapCatalog)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Catalog> {
        return catalogQueries.getById(id = id, mapper = ::mapCatalog)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Catalog> {
        return catalogQueries.getLast(mapper = ::mapCatalog)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    private fun mapCatalog(
        id: Long,
        productId: Long,
        name: String,
        photo: ByteArray,
        title: String,
        description: String,
        discount: Long,
        price: Double
    ) = Catalog(
        id = id,
        productId = productId,
        name = name,
        photo = photo,
        title = title,
        description = description,
        discount = discount,
        price = price.toFloat()
    )
}