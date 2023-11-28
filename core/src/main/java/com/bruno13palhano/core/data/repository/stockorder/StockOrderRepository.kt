package com.bruno13palhano.core.data.repository.stockorder

import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalStockOrderLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.network.access.StockOrderNetwork
import com.bruno13palhano.core.network.di.DefaultStockOrderNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class StockOrderRepository @Inject constructor(
    @DefaultStockOrderNet private val stockOrderNetwork: StockOrderNetwork,
    @InternalStockOrderLight private val stockOrderData: StockOrderData<StockOrder>,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : StockOrderData<StockOrder> {
    override suspend fun insert(model: StockOrder): Long {
        CoroutineScope(ioDispatcher).launch {
            stockOrderNetwork.insert(model)
        }
        return stockOrderData.insert(model = model)
    }

    override suspend fun update(model: StockOrder) {
        stockOrderData.update(model = model)
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getItems(isOrderedByCustomer = isOrderedByCustomer)
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.search(value = value, isOrderedByCustomer = isOrderedByCustomer)
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return stockOrderData.getByCategory(
            category = category,
            isOrderedByCustomer = isOrderedByCustomer
        )
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        stockOrderData.updateStockOrderQuantity(id = id, quantity = quantity)
    }

    override suspend fun deleteById(id: Long) {
        stockOrderData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<StockOrder>> {
        CoroutineScope(ioDispatcher).launch {
            stockOrderNetwork.getAll().forEach {
                println("item: id: ${it.id}, name: ${it.name}, categories: ${it.categories}")
                stockOrderData.insert(it)
            }
        }
        return stockOrderData.getAll()
    }

    override fun getDebitStock(): Flow<List<StockOrder>> {
        return stockOrderData.getDebitStock()
    }

    override fun getOutOfStock(): Flow<List<StockOrder>> {
        return stockOrderData.getOutOfStock()
    }

    override fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getStockOrderItems(isOrderedByCustomer)
    }

    override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getDebitStockByPrice(isOrderedAsc = isOrderedAsc)
    }

    override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return stockOrderData.getDebitStockByName(isOrderedAsc = isOrderedAsc)
    }

    override fun getById(id: Long): Flow<StockOrder> {
        return stockOrderData.getById(id = id)
    }

    override fun getLast(): Flow<StockOrder> {
        return stockOrderData.getLast()
    }
}