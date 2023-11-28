package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCategoryLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.di.DefaultCategoryNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class CategoryRepository @Inject constructor(
    @DefaultCategoryNet private val categoryNetwork: CategoryNetwork,
    @InternalCategoryLight private val categoryData: CategoryData<Category>,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
): CategoryData<Category> {
    override fun search(value: String): Flow<List<Category>> {
        return categoryData.search(value = value)
    }

    override suspend fun deleteById(id: Long) {
        categoryData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Category>> {
        CoroutineScope(ioDispatcher).launch {
            categoryNetwork.getAll().forEach { categoryData.insert(it) }
        }
        return categoryData.getAll()
    }

    override fun getById(id: Long): Flow<Category> {
        return categoryData.getById(id = id)
    }

    override fun getLast(): Flow<Category> {
        return categoryData.getLast()
    }

    override suspend fun update(model: Category) {
        CoroutineScope(ioDispatcher).launch {
            categoryNetwork.update(model)
        }
        return categoryData.update(model = model)
    }

    override suspend fun insert(model: Category): Long {
        CoroutineScope(ioDispatcher).launch {
            categoryNetwork.insert(model)
        }
        return categoryData.insert(model = model)
    }
}