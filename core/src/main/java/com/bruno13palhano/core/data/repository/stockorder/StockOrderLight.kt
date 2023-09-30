package com.bruno13palhano.core.data.repository.stockorder

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.StockOrderTableQueries
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class StockOrderLight @Inject constructor(
    private val stockOrderQueries: StockOrderTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockOrderData<StockOrder> {
    override suspend fun insert(model: StockOrder): Long {
        stockOrderQueries.insert(
            productId = model.productId,
            date = model.date,
            validity = model.validity,
            quantity = model.quantity.toLong(),
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            isOrderedByCustomer = model.isOrderedByCustomer,
            isPaid = model.isPaid
        )
        return stockOrderQueries.lastId().executeAsOne()
    }

    override suspend fun update(model: StockOrder) {
        stockOrderQueries.update(
            id = model.id,
            productId = model.productId,
            date = model.date,
            validity = model.validity,
            quantity = model.quantity.toLong(),
            purchasePrice = model.purchasePrice.toDouble(),
            salePrice = model.salePrice.toDouble(),
            isOrderedByCustomer = model.isOrderedByCustomer,
            isPaid = model.isPaid
        )
    }

    override suspend fun delete(model: StockOrder) {
        stockOrderQueries.delete(id = model.id)
    }

    override suspend fun insertItems(stockOrder: StockOrder, isPaid: Boolean) {
        stockOrderQueries.insert(
            productId = stockOrder.productId,
            date = stockOrder.date,
            purchasePrice = stockOrder.purchasePrice.toDouble(),
            validity = stockOrder.validity,
            quantity = stockOrder.quantity.toLong(),
            salePrice = stockOrder.salePrice.toDouble(),
            isOrderedByCustomer = stockOrder.isOrderedByCustomer,
            isPaid = stockOrder.isPaid
        )
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.getItems(
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderQueries.search(
            name = value,
            company = value,
            description = value,
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return stockOrderQueries.getByCategory(
            category = category,
            isOrderedByCustomer = isOrderedByCustomer,
            mapper = ::mapStockOrder
        ).asFlow().mapToList(ioDispatcher)
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        stockOrderQueries.updateStockOrderQuantity(quantity = quantity.toLong(), id = id)
    }

    override suspend fun deleteById(id: Long) {
        stockOrderQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<StockOrder>> {
        return stockOrderQueries.getAll(mapper = ::mapStockOrder)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getDebitStock(): Flow<List<StockOrder>> {
        return stockOrderQueries.getDebitStock(mapper = ::mapStockOrder)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderQueries.getById(id = id, mapper = ::mapStockOrder)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderQueries.getLast(mapper = ::mapStockOrder)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    private fun mapStockOrder(
        id: Long,
        productId: Long,
        name: String,
        photo: String,
        date: Long,
        validity: Long,
        quantity: Long,
        categories: List<Category>,
        company: String,
        purchasePrice: Double,
        salePrice: Double,
        isOrderedByCustomer: Boolean,
        isPaid: Boolean
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
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )
}