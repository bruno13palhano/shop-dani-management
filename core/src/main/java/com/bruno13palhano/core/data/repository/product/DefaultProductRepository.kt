package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalProduct
import com.bruno13palhano.core.data.di.InternalVersion
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.productNetToProduct
import com.bruno13palhano.core.data.repository.productToProductNet
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.data.repository.versionToVersionNet
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.access.RemoteProductData
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.di.FirebaseProduct
import com.bruno13palhano.core.network.di.FirebaseVersion
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultProductRepository
    @Inject
    constructor(
        @FirebaseProduct private val remoteProductData: RemoteProductData,
        @InternalProduct private val productData: ProductData,
        @InternalVersion private val versionData: VersionData,
        @FirebaseVersion private val remoteVersionData: RemoteVersionData,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    ) : ProductRepository {
        override suspend fun insert(
            model: Product,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ): Long {
            val productVersion = Versions.productVersion(timestamp = model.timestamp)

            val id =
                productData.insert(model = model, version = productVersion, onError = onError) {
                    val netModel =
                        productToProductNet(
                            Product(
                                id = it,
                                name = model.name,
                                code = model.code,
                                description = model.description,
                                photo = model.photo,
                                date = model.date,
                                categories = model.categories,
                                company = model.company,
                                timestamp = model.timestamp,
                            ),
                        )

                    CoroutineScope(ioDispatcher).launch {
                        try {
                            remoteProductData.insert(data = netModel)
                            remoteVersionData.insert(data = versionToVersionNet(productVersion))
                            onSuccess(netModel.id)
                        } catch (e: Exception) {
                            onError(Errors.INSERT_SERVER_ERROR)
                        }
                    }
                }

            return id
        }

        override suspend fun update(
            model: Product,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val productVersion = Versions.productVersion(timestamp = model.timestamp)

            productData.update(model = model, version = productVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteProductData.insert(data = productToProductNet(model))
                        remoteVersionData.insert(data = versionToVersionNet(productVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.UPDATE_SERVER_ERROR)
                    }
                }
            }
        }

        override fun search(value: String): Flow<List<Product>> {
            return productData.search(value = value)
        }

        override fun searchPerCategory(
            value: String,
            categoryId: Long,
        ): Flow<List<Product>> {
            return productData.searchPerCategory(value = value, categoryId = categoryId)
        }

        override fun getByCategory(category: String): Flow<List<Product>> {
            return productData.getByCategory(category = category)
        }

        override fun getByCode(code: String): Flow<List<Product>> {
            return productData.getByCode(code = code)
        }

        override suspend fun deleteById(
            id: Long,
            timestamp: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val productVersion = Versions.productVersion(timestamp = timestamp)

            productData.deleteById(id = id, version = productVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteProductData.delete(id = id)
                        remoteVersionData.update(data = versionToVersionNet(productVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.DELETE_SERVER_ERROR)
                    }
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
                dataVersion = getDataVersion(versionData, Versions.PRODUCT_VERSION_ID),
                networkVersion = getNetworkVersion(remoteVersionData, Versions.PRODUCT_VERSION_ID),
                dataList = getDataList(productData),
                networkList = getNetworkList(remoteProductData).map { productNetToProduct(it) },
                onPush = { deleteIds, saveList, dtVersion ->
                    deleteIds.forEach { remoteProductData.delete(it) }
                    saveList.forEach { remoteProductData.insert(productToProductNet(it)) }
                    remoteVersionData.insert(versionToVersionNet(dtVersion))
                },
                onPull = { deleteIds, saveList, netVersion ->
                    deleteIds.forEach { productData.deleteById(it, netVersion, {}) {} }
                    saveList.forEach { productData.insert(it, netVersion, {}) {} }
                    versionData.insert(netVersion, {}) {}
                },
            )
    }
