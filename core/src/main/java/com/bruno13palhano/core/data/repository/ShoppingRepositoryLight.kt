package com.bruno13palhano.core.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.ShoppingTableQueries
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Shopping
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingRepositoryLight @Inject constructor(
    private val shoppingQueries: ShoppingTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ShoppingData<Shopping> {
    override suspend fun insert(model: Shopping): Long {
        shoppingQueries.insert(
            productId = model.productId,
            name = model.name,
            purchasePrice = model.purchasePrice.toDouble(),
            quantity = model.quantity.toLong(),
            date = model.date,
            isPaid = model.isPaid
        )
        return 0L
    }

    override suspend fun update(model: Shopping) {
        shoppingQueries.update(
            id = model.id,
            productId = model.productId,
            name = model.name,
            purchasePrice = model.purchasePrice.toDouble(),
            quantity = model.quantity.toLong(),
            date = model.date,
            isPaid = model.isPaid
        )
    }

    override suspend fun delete(model: Shopping) {
        shoppingQueries.delete(model.id)
    }

    override fun getItemsLimited(offset: Int, limit: Int): Flow<List<Shopping>> {
        return shoppingQueries.getItemsLimited(
            offset.toLong(),
            limit.toLong(),
            mapper = ::mapShopping
        ).asFlow().mapToList(ioDispatcher)
    }

    override suspend fun deleteById(id: Long) {
        shoppingQueries.delete(id)
    }

    override fun getAll(): Flow<List<Shopping>> {
        return shoppingQueries.getLast(mapper = ::mapShopping)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Shopping> {
        return shoppingQueries.getById(id, mapper = ::mapShopping)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun getLast(): Flow<Shopping> {
        return shoppingQueries.getLast(mapper = ::mapShopping)
            .asFlow().mapToOne(ioDispatcher)
    }

    private fun mapShopping(
        id: Long,
        productId: Long,
        name: String,
        purchasePrice: Double,
        quantity: Long,
        date: Long,
        isPaid: Boolean
    ) = Shopping(
        id = id,
        productId = productId,
        name = name,
        purchasePrice = purchasePrice.toFloat(),
        quantity = quantity.toInt(),
        date = date,
        isPaid = isPaid
    )
}