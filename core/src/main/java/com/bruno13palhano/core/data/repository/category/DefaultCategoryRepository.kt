package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCategoryLight
import com.bruno13palhano.core.data.di.InternalVersionLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.Versions
import com.bruno13palhano.core.data.repository.categoryNetToCategory
import com.bruno13palhano.core.data.repository.categoryToCategoryNet
import com.bruno13palhano.core.data.repository.getDataList
import com.bruno13palhano.core.data.repository.getDataVersion
import com.bruno13palhano.core.data.repository.getNetworkList
import com.bruno13palhano.core.data.repository.getNetworkVersion
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.data.repository.versionToVersionNet
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.di.DefaultCategoryNet
import com.bruno13palhano.core.network.di.DefaultVersionNet
import com.bruno13palhano.core.sync.Synchronizer
import com.bruno13palhano.core.sync.syncData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultCategoryRepository
    @Inject
    constructor(
        @DefaultCategoryNet private val categoryNetwork: CategoryNetwork,
        @InternalCategoryLight private val categoryData: CategoryData,
        @InternalVersionLight private val versionData: VersionData,
        @DefaultVersionNet private val versionNetwork: VersionNetwork,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    ) : CategoryRepository {
        override fun search(value: String): Flow<List<Category>> {
            return categoryData.search(value = value)
        }

        override suspend fun deleteById(
            id: Long,
            timestamp: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val categoryVersion = Versions.categoryVersion(timestamp = timestamp)

            categoryData.deleteById(id = id, version = categoryVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        categoryNetwork.delete(id = id)
                        versionNetwork.update(data = versionToVersionNet(categoryVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.DELETE_SERVER_ERROR)
                    }
                }
            }
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
                dataVersion = getDataVersion(versionData, Versions.CATEGORY_VERSION_ID),
                networkVersion = getNetworkVersion(versionNetwork, Versions.CATEGORY_VERSION_ID),
                dataList = getDataList(categoryData),
                networkList = getNetworkList(categoryNetwork).map { categoryNetToCategory(it) },
                onPush = { deleteIds, saveList, dtVersion ->
                    deleteIds.forEach { categoryNetwork.delete(it) }
                    saveList.forEach { categoryNetwork.insert(categoryToCategoryNet(it)) }
                    versionNetwork.insert(versionToVersionNet(dtVersion))
                },
                onPull = { deleteIds, saveList, netVersion ->
                    deleteIds.forEach { categoryData.deleteById(it, netVersion, {}) {} }
                    saveList.forEach { categoryData.insert(it, netVersion, {}) {} }
                    versionData.insert(netVersion, {}) {}
                },
            )

        override suspend fun update(
            model: Category,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            val categoryVersion = Versions.categoryVersion(timestamp = model.timestamp)

            categoryData.update(model = model, version = categoryVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        categoryNetwork.update(data = categoryToCategoryNet(model))
                        versionNetwork.update(data = versionToVersionNet(categoryVersion))
                        onSuccess()
                    } catch (e: Exception) {
                        onError(Errors.UPDATE_SERVER_ERROR)
                    }
                }
            }
        }

        override suspend fun insert(
            model: Category,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ): Long {
            val categoryVersion = Versions.categoryVersion(timestamp = model.timestamp)

            val id =
                categoryData.insert(model = model, version = categoryVersion, onError = onError) {
                    val netModel =
                        categoryToCategoryNet(
                            Category(
                                id = it,
                                category = model.category,
                                timestamp = model.timestamp,
                            ),
                        )

                    CoroutineScope(ioDispatcher).launch {
                        try {
                            categoryNetwork.insert(data = netModel)
                            versionNetwork.insert(data = versionToVersionNet(categoryVersion))
                            onSuccess(netModel.id)
                        } catch (e: Exception) {
                            onError(Errors.INSERT_SERVER_ERROR)
                        }
                    }
                }

            return id
        }
    }
