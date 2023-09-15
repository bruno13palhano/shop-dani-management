package com.bruno13palhano.core.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.StockOrderTableQueries
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockOrderRepositoryLight @Inject constructor(
    private val stockOrderQueries: StockOrderTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockOrderData<StockOrder> {
    override suspend fun insert(model: StockOrder): Long {
        stockOrderQueries.insert(
            productId = model.productId,
            name = model.name,
            photo = model.photo,
            date = model.date,
            validity = model.validity,
            quantity = model.quantity.toLong(),
            categories = model.categories,
            company = model.company,
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            isOrderedByCustomer = model.isOrderedByCustomer
        )
        return 0L
    }

    override suspend fun update(model: StockOrder) {
        stockOrderQueries.update(
            id = model.id,
            productId = model.productId,
            name = model.name,
            photo = model.photo,
            date = model.date,
            validity = model.validity,
            quantity = model.quantity.toLong(),
            categories = model.categories,
            company = model.company,
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            isOrderedByCustomer = model.isOrderedByCustomer
        )
    }

    override suspend fun delete(model: StockOrder) {
        stockOrderQueries.delete(model.id)
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.getItems(
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.search(
            value,
            value,
            value,
            isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return stockOrderQueries.getByCategory(
            category,
            isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        stockOrderQueries.updateStockOrderQuantity(quantity.toLong(), id)
    }

    override suspend fun deleteById(id: Long) {
        stockOrderQueries.delete(id)
    }

    override fun getAll(): Flow<List<StockOrder>> {
        return stockOrderQueries.getAll(mapper = ::mapStockOrder)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderQueries.getById(id = id, mapper = ::mapStockOrder)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderQueries.getLast(mapper = ::mapStockOrder)
            .asFlow().mapToOne(ioDispatcher)
    }

    private fun mapStockOrder(
        id: Long,
        productId: Long,
        name: String,
        photo: String,
        date: Long,
        validity: Long,
        quantity: Long,
        categories: List<String>,
        company: String,
        purchasePrice: Double,
        salePrice: Double,
        isOrderedByCustomer: Boolean
    ) = StockOrder(
        id = id,
        productId = productId,
        name = name,
        photo = photo,
        date = date,
        validity = validity,
        quantity = quantity.toInt(),
        categories = categories,
        company = company,
        purchasePrice = purchasePrice.toFloat(),
        salePrice = salePrice.toFloat(),
        isOrderedByCustomer = isOrderedByCustomer
    )
}