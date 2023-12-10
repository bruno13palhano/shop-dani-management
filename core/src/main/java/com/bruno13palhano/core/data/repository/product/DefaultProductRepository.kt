package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalProductLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultProductNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultProductRepository @Inject constructor(
    @DefaultProductNet private val productNetwork: ProductNetwork,
    @InternalProductLight private val productData: ProductData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
): ProductRepository {
    override suspend fun insert(model: Product): Long {
        val productVersion = Versions.productVersion(timestamp = model.timestamp)

        val id = productData.insert(model = model) {
            val netModel = Product(
                id = it,
                name = model.name,
                code = model.code,
                description = model.description,
                photo = model.photo,
                date = model.date,
                categories = model.categories,
                company = model.company,
                timestamp = model.timestamp
            )

            CoroutineScope(ioDispatcher).launch {
                productNetwork.insert(data = netModel)
            }
        }

        versionData.insert(productVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.insert(data = productVersion)
            }
        }

        return id
    }

    override suspend fun update(model: Product) {
        val productVersion = Versions.productVersion(timestamp = model.timestamp)

        productData.update(model = model) {
            CoroutineScope(ioDispatcher).launch {
                productNetwork.insert(data = model)
            }
        }

        versionData.update(model = productVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.insert(data = productVersion)
            }
        }
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

    override suspend fun deleteById(id: Long, timestamp: String) {
        val productVersion = Versions.productVersion(timestamp = timestamp)

        productData.deleteById(id = id) {
            CoroutineScope(ioDispatcher).launch {
                productNetwork.delete(id = id)
            }
        }

        versionData.update(model = productVersion) {
            CoroutineScope(ioDispatcher).launch {
                versionNetwork.update(data = productVersion)
            }
        }
    }

    override fun getAll(): Flow<List<Product>> {
        return productData.getAll()
    }

    override fun getById(id: Long): Flow<Product> {
        return productData.getById(id = id)
    }

    override fun getLast(): Flow<Product> {
        return productData.getLast()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.syncData(
            dataVersion = getDataVersion(versionData, 2L),
            networkVersion = getNetworkVersion(versionNetwork, 2L),
            dataList = getDataList(productData),
            networkList = getNetworkList(productNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { productNetwork.delete(it) }
                saveList.forEach { productNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { productData.deleteById(it) {} }
                saveList.forEach { productData.insert(it) {} }
                versionData.insert(netVersion) {}
            }
        )
}