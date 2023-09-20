package com.bruno13palhano.core.data.repository.shopping

import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.InternalShoppingLight
import com.bruno13palhano.core.model.Shopping
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ShoppingRepository @Inject constructor(
    @InternalShoppingLight private val shoppingData: InternalShoppingData
) : ShoppingData<Shopping> {
    override suspend fun insert(model: Shopping): Long {
        return shoppingData.insert(model = model)
    }

    override suspend fun update(model: Shopping) {
        shoppingData.update(model = model)
    }

    override suspend fun delete(model: Shopping) {
        shoppingData.delete(model = model)
    }

    override fun getItemsLimited(offset: Int, limit: Int): Flow<List<Shopping>> {
        return shoppingData.getItemsLimited(offset = offset, limit = limit)
    }

    override suspend fun deleteById(id: Long) {
        shoppingData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Shopping>> {
        return shoppingData.getAll()
    }

    override fun getById(id: Long): Flow<Shopping> {
        return shoppingData.getById(id = id)
    }

    override fun getLast(): Flow<Shopping> {
        return shoppingData.getLast()
    }
}