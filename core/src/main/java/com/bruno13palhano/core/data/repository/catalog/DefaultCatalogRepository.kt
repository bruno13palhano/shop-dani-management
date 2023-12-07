package com.bruno13palhano.core.data.repository.catalog

import com.bruno13palhano.core.data.di.InternalCatalogLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
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

internal class DefaultCatalogRepository @Inject constructor(
    @DefaultCatalogNet private val catalogNetwork: CatalogNetwork,
    @InternalCatalogLight private val catalogData: CatalogData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
) : CatalogRepository {
    override suspend fun insert(model: Catalog): Long {
        versionData.insert(DataVersion(7L, "CATALOG", model.timestamp))
        return catalogData.insert(model = model)
    }

    override suspend fun update(model: Catalog) {
        versionData.update(DataVersion(7L, "CATALOG", model.timestamp))
        catalogData.update(model = model)
    }

    override suspend fun deleteById(id: Long) {
        versionData.update(DataVersion(7L, "CATALOG", OffsetDateTime.now()))
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
                deleteIds.forEach { catalogData.deleteById(it) }
                saveList.forEach { catalogData.insert(it) }
                versionData.insert(netVersion)
            }
        )
}