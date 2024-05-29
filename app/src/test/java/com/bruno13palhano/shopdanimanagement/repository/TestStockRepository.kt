package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestStockRepository : StockRepository {
    private val stockItemList = mutableListOf<StockItem>()

    override fun getItems(): Flow<List<StockItem>> {
        return flowOf(stockItemList)
    }

    override fun search(value: String): Flow<List<StockItem>> {
        return flowOf(stockItemList.filter { item -> item.name == value || item.company == value })
    }

    override fun getByCategory(category: String): Flow<List<StockItem>> {
        return flowOf(
            stockItemList.filter { item ->
                item.categories.joinToString(", ") { it.category }.contains(category)
            }
        )
    }

    override suspend fun updateStockQuantity(
        id: Long,
        quantity: Int,
        timestamp: String
    ) {
        val index = getIndex(id = id, list = stockItemList)

        if (isIndexValid(index = index)) {
            val currentItem = stockItemList[index]

            val item =
                StockItem(
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
                    isPaid = currentItem.isPaid,
                    timestamp = currentItem.timestamp
                )

            stockItemList[index] = item
        }
    }

    override fun getStockItems(): Flow<List<StockItem>> {
        return flowOf(stockItemList.filter { it.quantity > 0 })
    }

    override fun getDebitStock(): Flow<List<StockItem>> = flowOf(stockItemList.filter { !it.isPaid })

    override fun getOutOfStock(): Flow<List<StockItem>> = flowOf(stockItemList.filter { it.quantity == 0 })

    override fun getDebitStockByPrice(isOrderedAsc: Boolean): Flow<List<StockItem>> {
        return if (isOrderedAsc) {
            flowOf(
                stockItemList.filter { !it.isPaid }.sortedBy { it.purchasePrice }
            )
        } else {
            flowOf(stockItemList.filter { !it.isPaid }.sortedByDescending { it.purchasePrice })
        }
    }

    override fun getDebitStockByName(isOrderedAsc: Boolean): Flow<List<StockItem>> {
        return if (isOrderedAsc) {
            flowOf(stockItemList.filter { !it.isPaid }.sortedBy { it.name })
        } else {
            flowOf(stockItemList.filter { !it.isPaid }.sortedByDescending { it.name })
        }
    }

    override suspend fun insert(
        model: StockItem,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        stockItemList.add(model)
        onSuccess(model.id)
        return model.id
    }

    override suspend fun update(
        model: StockItem,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = model.id, list = stockItemList)
        if (isIndexValid(index = index)) {
            stockItemList[index] = model
            onSuccess()
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = id, list = stockItemList)
        if (isIndexValid(index = index)) {
            stockItemList.removeAt(index = index)
            onSuccess()
        }
    }

    override fun getAll(): Flow<List<StockItem>> = flowOf(stockItemList)

    override fun getById(id: Long): Flow<StockItem> {
        val index = getIndex(id = id, list = stockItemList)
        return if (isIndexValid(index = index)) flowOf(stockItemList[index]) else flowOf()
    }

    override fun getLast(): Flow<StockItem> = flowOf(stockItemList.last())

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}