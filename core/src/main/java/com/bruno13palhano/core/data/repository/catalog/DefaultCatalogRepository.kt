package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCatalogLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
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

internal class DefaultCatalogRepository @Inject constructor(
    @DefaultCatalogNet private val catalogNetwork: CatalogNetwork,
    @InternalCatalogLight private val catalogData: CatalogData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CatalogRepository {
    override suspend fun insert(
        model: Catalog,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        val catalogVersion = Versions.catalogVersion(timestamp = model.timestamp)

        val id = catalogData.insert(model = model, onError = onError) {
            val netModel = Catalog(
                id = it,
                productId = model.productId,
                name = model.name,
                photo = model.photo,
                title = model.title,
                description = model.description,
                discount = model.discount,
                price = model.price,
                timestamp = model.timestamp
            )

            try {
                CoroutineScope(ioDispatcher).launch {
                    catalogNetwork.insert(data = netModel)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(4)
            }
        }

        versionData.insert(model = catalogVersion, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    versionNetwork.insert(data = catalogVersion)
                    onSuccess(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(4)
            }
        }

        return id
    }

    override suspend fun update(
        model: Catalog,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val catalogVersion = Versions.catalogVersion(timestamp = model.timestamp)

        catalogData.update(model = model, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    catalogNetwork.update(data = model)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(5)
            }
        }

        versionData.update(model = catalogVersion, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    versionNetwork.update(data = catalogVersion)
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(5)
            }
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val catalogVersion = Versions.catalogVersion(timestamp = timestamp)

        catalogData.deleteById(id = id, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    catalogNetwork.delete(id = id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(6)
            }
        }

        versionData.update(model = catalogVersion, onError = onError) {
            try {
                CoroutineScope(ioDispatcher).launch {
                    versionNetwork.update(data = catalogVersion)
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(6)
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
            dataVersion = getDataVersion(versionData, 7L),
            networkVersion = getNetworkVersion(versionNetwork, 7L),
            dataList = getDataList(catalogData),
            networkList = getNetworkList(catalogNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { catalogNetwork.delete(it) }
                saveList.forEach { catalogNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { catalogData.deleteById(it, {}) {} }
                saveList.forEach { catalogData.insert(it, {}) {} }
                versionData.insert(netVersion, {}) {}
            }
        )
}