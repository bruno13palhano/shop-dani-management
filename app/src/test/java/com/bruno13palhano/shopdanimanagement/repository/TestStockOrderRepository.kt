package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.model.StockOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestStockOrderRepository : StockOrderData<StockOrder> {
    private val stockOrderList = mutableListOf<StockOrder>()

    override suspend fun insert(model: StockOrder): Long {
        stockOrderList.add(model)
        return model.id
    }

    override suspend fun update(model: StockOrder) {
        val index = getIndex(id = model.id, list = stockOrderList)
        if (isIndexValid(index = index)) stockOrderList[index] = model
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedByCustomer) {
            flowOf(stockOrderList.filter { it.isOrderedByCustomer })
        } else {
            flowOf(stockOrderList.filter { !it.isOrderedByCustomer })
        }
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedByCustomer) {
            flowOf(
                stockOrderList.filter { order ->
                    order.isOrderedByCustomer && (order.name == value || order.company == value)
                }
            )
        } else {
            flowOf(
                stockOrderList.filter { item ->
                    !item.isOrderedByCustomer && (item.name == value || item.company == value)
                }
            )
        }
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockOrder>> {
        return if (isOrderedByCustomer) {
            flowOf(
                stockOrderList.filter { order ->
                    order.isOrderedByCustomer &&
                            (order.categories.joinToString(", ") { it.name }
                                .contains(category))
                }
            )
        } else {
            flowOf(
                stockOrderList.filter { item ->
                    !item.isOrderedByCustomer &&
                            (item.categories.joinToString(", ") { it.name }
                                .contains(category))
                }
            )
        }
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        val index = getIndex(id = id, list = stockOrderList)

        if (isIndexValid(index = index)) {
            val currentItem = stockOrderList[index]

            val item = StockOrder(
                id = currentItem.id,
                productId = currentItem.productId,
                name = currentItem.name,
                photo = currentItem.photo,
                date = currentItem.date,
                validity = currentItem.validity,
                quantity = quantity,
                categories = currentItem.categories,
                company = currentItem.company,
                purchasePrice = currentItem.purchasePrice,
                salePrice = currentItem.salePrice,
                isOrderedByCustomer = currentItem.isOrderedByCustomer,
                isPaid = currentItem.isPaid
            )

            stockOrderList[index] = item
        }
    }

    override fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedByCustomer) {
            flowOf(stockOrderList.filter { it.quantity > 0 && it.isOrderedByCustomer })
        } else {
            flowOf(stockOrderList.filter { it.quantity > 0 && !it.isOrderedByCustomer })
        }
    }

    override fun getDebitStock(): Flow<List<StockOrder>> =
        flowOf(stockOrderList.filter { !it.isPaid && !it.isOrderedByCustomer})

    override fun getOutOfStock(): Flow<List<StockOrder>> =
        flowOf(stockOrderList.filter { it.quantity == 0 && !it.isOrderedByCustomer})


    override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedAsc) {
            flowOf(
                stockOrderList.filter { !it.isPaid && it.isOrderedByCustomer}
                    .sortedBy { it.salePrice }
            )
        } else {
            flowOf(stockOrderList.filter { !it.isPaid && it.isOrderedByCustomer}
                .sortedBy { it.salePrice })
        }
    }

    override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockOrder>> {
        return if (isOrderedAsc) {
            flowOf(
                stockOrderList.filter { !it.isPaid && it.isOrderedByCustomer}.sortedBy { it.name }
            )
        } else {
            flowOf(
                stockOrderList.filter { !it.isPaid && it.isOrderedByCustomer}.sortedBy { it.name }
            )
        }
    }

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = stockOrderList)
        if (isIndexValid(index = index)) stockOrderList.removeAt(index = index)
    }

    override fun getAll(): Flow<List<StockOrder>> = flowOf(stockOrderList)

    override fun getById(id: Long): Flow<StockOrder> {
        val index = getIndex(id = id, list = stockOrderList)
        return if (isIndexValid(index = index)) flowOf(stockOrderList[index]) else flowOf()
    }

    override fun getLast(): Flow<StockOrder> = flowOf(stockOrderList.last())
}