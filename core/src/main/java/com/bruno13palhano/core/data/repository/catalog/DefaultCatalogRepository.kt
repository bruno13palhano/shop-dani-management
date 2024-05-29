package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCatalogLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.catalogNetToCatalog
import com.bruno13palhano.core.data.repository.catalogToCatalogNet
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.data.repository.versionToVersionNet
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultCatalogNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultCatalogRepository
    @Inject
    constructor(
        @DefaultCatalogNet private val catalogNetwork: CatalogNetwork,
        @InternalCatalogLight private val catalogData: CatalogData,
        @InternalVersionLight private val versionData: VersionData,
        @DefaultVersionNet private val versionNetwork: VersionNetwork,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    ) : CatalogRepository {
        override suspend fun insert(
            model: Catalog,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ): Long {
            val catalogVersion = Versions.catalogVersion(timestamp = model.timestamp)

            val id =
                catalogData.insert(model = model, version = catalogVersion, onError = onError) {
                    val netModel =
                        catalogToCatalogNet(
                            Catalog(
                                id = it,
                                productId = model.productId,
                                name = model.name,
                                photo = model.photo,
                                title = model.title,
                                description = model.description,
                                discount = model.discount,
                                price = model.price,
                                timestamp = model.timestamp,
                            ),
                        )

                    CoroutineScope(ioDispatcher).launch {
                        try {
                            catalogNetwork.insert(data = netModel)
                            versionNetwork.insert(data = versionToVersionNet(catalogVersion))
                            onSuccess(netModel.id)
                        } catch (e: Exception) {
                            onError(4)
                        }
                    }
                }

            return id
        }

        override suspend fun update(
            model: Catalog,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val catalogVersion = Versions.catalogVersion(timestamp = model.timestamp)

            catalogData.update(model = model, version = catalogVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        catalogNetwork.update(data = catalogToCatalogNet(model))
                        versionNetwork.update(data = versionToVersionNet(catalogVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(5)
                    }
                }
            }
        }

        override suspend fun deleteById(
            id: Long,
            timestamp: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val catalogVersion = Versions.catalogVersion(timestamp = timestamp)

            catalogData.deleteById(id = id, version = catalogVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        catalogNetwork.delete(id = id)
                        versionNetwork.update(data = versionToVersionNet(catalogVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(6)
                    }
                }
            }
        }

        override fun getAll(): Flow<List<Catalog>> {
            return catalogData.getAll()
        }

        override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Catalog>> {
            return catalogData.getOrderedByName(isOrderedAsc = isOrderedAsc)
        }

        override fun getOrderedByPrice(isOrderedAsc: Boolean): Flow<List<Catalog>> {
            return catalogData.getOrderedByPrice(isOrderedAsc = isOrderedAsc)
        }

        override fun getById(id: Long): Flow<Catalog> {
            return catalogData.getById(id = id)
        }

        override fun getLast(): Flow<Catalog> {
            return catalogData.getLast()
        }

        override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
            synchronizer.syncData(
                dataVersion = getDataVersion(versionData, Versions.CATALOG_VERSION_ID),
                networkVersion = getNetworkVersion(versionNetwork, Versions.CATALOG_VERSION_ID),
                dataList = getDataList(catalogData),
                networkList = getNetworkList(catalogNetwork).map { catalogNetToCatalog(it) },
                onPush = { deleteIds, saveList, dtVersion ->
                    deleteIds.forEach { catalogNetwork.delete(it) }
                    saveList.forEach { catalogNetwork.insert(catalogToCatalogNet(it)) }
                    versionNetwork.insert(versionToVersionNet(dtVersion))
                },
                onPull = { deleteIds, saveList, netVersion ->
                    deleteIds.forEach { catalogData.deleteById(it, netVersion, {}) {} }
                    saveList.forEach { catalogData.insert(it, netVersion, {}) {} }
                    versionData.insert(netVersion, {}) {}
                },
            )
    }
