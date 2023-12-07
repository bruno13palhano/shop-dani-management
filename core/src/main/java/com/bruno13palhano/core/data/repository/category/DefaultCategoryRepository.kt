package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.di.InternalCategoryLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultCategoryNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import javax.inject.Inject

internal class DefaultCategoryRepository @Inject constructor(
    @DefaultCategoryNet private val categoryNetwork: CategoryNetwork,
    @InternalCategoryLight private val categoryData: CategoryData,
    @InternalVersionLight private val versionData: VersionData,
    @DefaultVersionNet private val versionNetwork: VersionNetwork,
): CategoryRepository {
    override fun search(value: String): Flow<List<Category>> {
        return categoryData.search(value = value)
    }

    override suspend fun deleteById(id: Long) {
        versionData.update(DataVersion(1L, "CATEGORY", OffsetDateTime.now()))
        categoryData.deleteById(id = id)
    }

    override fun getAll(): Flow<List<Category>> {
        return categoryData.getAll()
    }

    override fun getById(id: Long): Flow<Category> {
        return categoryData.getById(id = id)
    }

    override fun getLast(): Flow<Category> {
        return categoryData.getLast()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.syncData(
            dataVersion = getDataVersion(versionData, 1L),
            networkVersion = getNetworkVersion(versionNetwork, 1L),
            dataList = getDataList(categoryData),
            networkList = getNetworkList(categoryNetwork),
            onPush = { deleteIds, saveList, dtVersion ->
                deleteIds.forEach { categoryNetwork.delete(it) }
                saveList.forEach { categoryNetwork.insert(it) }
                versionNetwork.insert(dtVersion)
            },
            onPull = { deleteIds, saveList, netVersion ->
                deleteIds.forEach { categoryData.deleteById(it) }
                saveList.forEach { categoryData.insert(it) }
                versionData.insert(netVersion)
            }
        )

    override suspend fun update(model: Category) {
        versionData.update(DataVersion(1L, "CATEGORY", model.timestamp))
        return categoryData.update(model = model)
    }

    override suspend fun insert(model: Category): Long {
        versionData.insert(DataVersion(1L, "CATEGORY", model.timestamp))
        return categoryData.insert(model = model)
    }
}