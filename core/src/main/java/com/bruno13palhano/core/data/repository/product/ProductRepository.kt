package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.di.InternalProductLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultProductNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

internal class ProductRepository @Inject constructor(
    @DefaultProductNet private val productNetwork: ProductNetwork,
    @InternalProductLight private val productData: ProductData<Product>,
    @InternalVersionLight private val versionLight: VersionData<DataVersion>,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
): ProductData<Product> {
    override suspend fun insert(model: Product): Long {
        versionLight.insert(DataVersion(2L, "PRODUCT", model.timestamp))
        return productData.insert(model = model)
    }

    override suspend fun update(model: Product) {
        versionLight.update(DataVersion(2L, "PRODUCT", OffsetDateTime.now()))
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
        versionLight.update(DataVersion(2L, "PRODUCT", OffsetDateTime.now()))
        return productData.deleteById(id = id)
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
            dataVersion = getDataVersion(versionLight, 2L),
            networkVersion = getNetworkVersion(versionNetwork, 2L),
            dataList = getDataList(productData),
            networkList = getNetworkList(productNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { productNetwork.delete(it) }
                saveList.forEach { productNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { productData.deleteById(it) }
                saveList.forEach { productData.insert(it) }
                versionLight.insert(netVersion)
            }
        )
}