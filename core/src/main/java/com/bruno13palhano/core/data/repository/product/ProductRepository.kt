package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalProductLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.di.DefaultProductNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ProductRepository @Inject constructor(
    @DefaultProductNet private val productNetwork: ProductNetwork,
    @InternalProductLight private val productData: ProductData<Product>,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
): ProductData<Product> {
    override suspend fun insert(model: Product): Long {
        CoroutineScope(ioDispatcher).launch {
            productNetwork.insert(model)
        }
        return productData.insert(model = model)
    }

    override suspend fun update(model: Product) {
        println(productNetwork.getAll())
        productData.update(model = model)
    }

    override fun search(value: String): Flow<List<Product>> {
        return productData.search(value = value)
    }

    override fun searchPerCategory(value: String, categoryId: Long): Flow<List<Product>> {
        return productData.searchPerCategory(value = value, categoryId = categoryId)
    }

    override fun getByCategory(category: String): Flow<List<Product>> {
        return productData.getByCategory(category = category)
    }

    override suspend fun deleteById(id: Long) {
        return productData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Product>> {
        CoroutineScope(ioDispatcher).launch {
            productNetwork.getAll().map {
                println(it)
                productData.insert(it)
            }
        }
        return productData.getAll()
    }

    override fun getById(id: Long): Flow<Product> {
        return productData.getById(id = id)
    }

    override fun getLast(): Flow<Product> {
        return productData.getLast()
    }
}