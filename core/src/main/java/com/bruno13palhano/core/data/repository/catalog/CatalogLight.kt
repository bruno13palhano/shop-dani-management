package com.bruno13palhano.core.data.repository.catalog

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.CatalogTableQueries
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.OffsetDateTime
import javax.inject.Inject

internal class CatalogLight @Inject constructor(
    private val catalogQueries: CatalogTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CatalogData<Catalog> {
    override suspend fun insert(model: Catalog): Long {
        if (model.isNew()) {
            catalogQueries.insert(
                productId = model.productId,
                title = model.title,
                description = model.description,
                discount = model.discount,
                price = model.price.toDouble(),
                timestamp = model.timestamp.toString()
            )
        } else {
            catalogQueries.insertWithId(
                id = model.id,
                productId = model.productId,
                title = model.title,
                description = model.description,
                discount = model.discount,
                price = model.price.toDouble(),
                timestamp = model.timestamp.toString()
            )
        }

        return catalogQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Catalog) {
        catalogQueries.update(
            title = model.title,
            description = model.description,
            discount = model.discount,
            price = model.price.toDouble(),
            id = model.id,
            timestamp = model.timestamp.toString()
        )
    }

    override suspend fun deleteById(id: Long) {
        catalogQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<Catalog>> {
        return catalogQueries.getAll(mapper = ::mapCatalog)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>> {
        return if (isOrderedAsc) {
            catalogQueries.getByNameAsc(mapper = ::mapCatalog)
                .asFlow().mapToList(ioDispatcher)
        } else {
            catalogQueries.getByNameDesc(mapper = ::mapCatalog)
                .asFlow().mapToList(ioDispatcher)
        }
    }

    override fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>> {
        return if (isOrderedAsc) {
            catalogQueries.getByPriceAsc(mapper = ::mapCatalog)
                .asFlow().mapToList(ioDispatcher)
        } else {
            catalogQueries.getByPriceDesc(mapper = ::mapCatalog)
                .asFlow().mapToList(ioDispatcher)
        }
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
        price: Double,
        timestamp: String
    ) = Catalog(
        id = id,
        productId = productId,
        name = name,
        photo = photo,
        title = title,
        description = description,
        discount = discount,
        price = price.toFloat(),
        timestamp = OffsetDateTime.parse(timestamp)
    )
}