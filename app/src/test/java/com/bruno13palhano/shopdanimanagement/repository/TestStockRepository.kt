package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.model.StockItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestStockOrderRepository : StockOrderRepository<StockItem> {
    private val stockItemList = mutableListOf<StockItem>()

    override suspend fun insert(model: StockItem): Long {
        stockItemList.add(model)
        return model.id
    }

    override suspend fun update(model: StockItem) {
        val index = getIndex(id = model.id, list = stockItemList)
        if (isIndexValid(index = index)) stockItemList[index] = model
    }

    override fun getItems(isOrderedByCustomer: Boolean): Flow<List<StockItem>> {
        return if (isOrderedByCustomer) {
            flowOf(stockItemList.filter { it.isOrderedByCustomer })
        } else {
            flowOf(stockItemList.filter { !it.isOrderedByCustomer })
        }
    }

    override fun search(value: String, isOrderedByCustomer: Boolean): Flow<List<StockItem>> {
        return if (isOrderedByCustomer) {
            flowOf(
                stockItemList.filter { order ->
                    order.isOrderedByCustomer && (order.name == value || order.company == value)
                }
            )
        } else {
            flowOf(
                stockItemList.filter { item ->
                    !item.isOrderedByCustomer && (item.name == value || item.company == value)
                }
            )
        }
    }

    override fun getByCategory(
        category: String,
        isOrderedByCustomer: Boolean
    ): Flow<List<StockItem>> {
        return if (isOrderedByCustomer) {
            flowOf(
                stockItemList.filter { order ->
                    order.isOrderedByCustomer &&
                            (order.categories.joinToString(", ") { it.category }
                                .contains(category))
                }
            )
        } else {
            flowOf(
                stockItemList.filter { item ->
                    !item.isOrderedByCustomer &&
                            (item.categories.joinToString(", ") { it.category }
                                .contains(category))
                }
            )
        }
    }

    override suspend fun updateStockOrderQuantity(id: Long, quantity: Int) {
        val index = getIndex(id = id, list = stockItemList)

        if (isIndexValid(index = index)) {
            val currentItem = stockItemList[index]

            val item = StockItem(
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

            stockItemList[index] = item
        }
    }

    override fun getStockOrderItems(isOrderedByCustomer: Boolean): Flow<List<StockItem>> {
        return if (isOrderedByCustomer) {
            flowOf(stockItemList.filter { it.quantity > 0 && it.isOrderedByCustomer })
        } else {
            flowOf(stockItemList.filter { it.quantity > 0 && !it.isOrderedByCustomer })
        }
    }

    override fun getDebitStock(): Flow<List<StockItem>> =
        flowOf(stockItemList.filter { !it.isPaid && !it.isOrderedByCustomer})

    override fun getOutOfStock(): Flow<List<StockItem>> =
        flowOf(stockItemList.filter { it.quantity == 0 && !it.isOrderedByCustomer})


    override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>> {
        return if (isOrderedAsc) {
            flowOf(
                stockItemList.filter { !it.isPaid && !it.isOrderedByCustomer}
                    .sortedBy { it.purchasePrice }
            )
        } else {
            flowOf(stockItemList.filter { !it.isPaid && !it.isOrderedByCustomer}
                .sortedByDescending { it.purchasePrice })
        }
    }

    override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>> {
        return if (isOrderedAsc) {
            flowOf(
                stockItemList.filter { !it.isPaid && !it.isOrderedByCustomer}.sortedBy { it.name }
            )
        } else {
            flowOf(
                stockItemList.filter { !it.isPaid && !it.isOrderedByCustomer}
                    .sortedByDescending { it.name }
            )
        }
    }

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = stockItemList)
        if (isIndexValid(index = index)) stockItemList.removeAt(index = index)
    }

    override fun getAll(): Flow<List<StockItem>> = flowOf(stockItemList)

    override fun getById(id: Long): Flow<StockItem> {
        val index = getIndex(id = id, list = stockItemList)
        return if (isIndexValid(index = index)) flowOf(stockItemList[index]) else flowOf()
    }

    override fun getLast(): Flow<StockItem> = flowOf(stockItemList.last())
}