package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.di.InternalCatalogLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultCatalogNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

internal class CatalogRepository @Inject constructor(
    @DefaultCatalogNet private val catalogNetwork: CatalogNetwork,
    @InternalCatalogLight private val catalogData: CatalogData<Catalog>,
    @InternalVersionLight private val versionLight: VersionData<DataVersion>,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
) : CatalogData<Catalog> {
    override suspend fun insert(model: Catalog): Long {
        versionLight.insert(DataVersion(7L, "CATALOG", model.timestamp))
        return catalogData.insert(model = model)
    }

    override suspend fun update(model: Catalog) {
        versionLight.update(DataVersion(7L, "CATALOG", model.timestamp))
        catalogData.update(model = model)
    }

    override suspend fun deleteById(id: Long) {
        versionLight.update(DataVersion(7L, "CATALOG", OffsetDateTime.now()))
        catalogData.deleteById(id = id)
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
            dataVersion = getDataVersion(versionLight, 7L),
            networkVersion = getNetworkVersion(versionNetwork, 7L),
            dataList = getDataList(catalogData),
            networkList = getNetworkList(catalogNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { catalogNetwork.delete(it) }
                saveList.forEach { catalogNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { catalogData.deleteById(it) }
                saveList.forEach { catalogData.insert(it) }
                versionLight.insert(netVersion)
            }
        )
}