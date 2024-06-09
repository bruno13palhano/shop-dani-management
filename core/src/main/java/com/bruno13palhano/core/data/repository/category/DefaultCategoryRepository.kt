package com.bruno13palhano.core.data.repository.category

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalCategory
import com.bruno13palhano.core.data.di.InternalVersion
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
import com.bruno13palhano.core.network.access.RemoteCategoryData
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.di.FirebaseCategory
import com.bruno13palhano.core.network.di.FirebaseVersion
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
        @FirebaseCategory private val remoteCategoryData: RemoteCategoryData,
        @InternalCategory private val categoryData: CategoryData,
        @InternalVersion private val versionData: VersionData,
        @FirebaseVersion private val remoteVersionData: RemoteVersionData,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : CategoryRepository {
        override fun search(value: String): Flow<List<Category>> {
            return categoryData.search(value = value)
        }

        override suspend fun deleteById(
            id: Long,
            timestamp: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            val categoryVersion = Versions.categoryVersion(timestamp = timestamp)

            categoryData.deleteById(id = id, version = categoryVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteCategoryData.delete(id = id)
                        remoteVersionData.update(data = versionToVersionNet(categoryVersion))
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
                networkVersion = getNetworkVersion(remoteVersionData, Versions.CATEGORY_VERSION_ID),
                dataList = getDataList(categoryData),
                networkList = getNetworkList(remoteCategoryData).map { categoryNetToCategory(it) },
                onPush = { deleteIds, saveList, dtVersion ->
                    deleteIds.forEach { remoteCategoryData.delete(it) }
                    saveList.forEach { remoteCategoryData.insert(categoryToCategoryNet(it)) }
                    remoteVersionData.insert(versionToVersionNet(dtVersion))
                },
                onPull = { deleteIds, saveList, netVersion ->
                    deleteIds.forEach { categoryData.deleteById(it, netVersion, {}) {} }
                    saveList.forEach { categoryData.insert(it, netVersion, {}) {} }
                    versionData.insert(netVersion, {}) {}
                }
            )

        override suspend fun update(
            model: Category,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            val categoryVersion = Versions.categoryVersion(timestamp = model.timestamp)

            categoryData.update(model = model, version = categoryVersion, onError = onError) {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        remoteCategoryData.update(data = categoryToCategoryNet(model))
                        remoteVersionData.update(data = versionToVersionNet(categoryVersion))
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
            onSuccess: (id: Long) -> Unit
        ): Long {
            val categoryVersion = Versions.categoryVersion(timestamp = model.timestamp)

            val id =
                categoryData.insert(model = model, version = categoryVersion, onError = onError) {
                    val netModel =
                        categoryToCategoryNet(
                            Category(
                                id = it,
                                category = model.category,
                                timestamp = model.timestamp
                            )
                        )

                    CoroutineScope(ioDispatcher).launch {
                        try {
                            remoteCategoryData.insert(data = netModel)
                            remoteVersionData.insert(data = versionToVersionNet(categoryVersion))
                            onSuccess(netModel.id)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            onError(Errors.INSERT_SERVER_ERROR)
                        }
                    }
                }

            return id
        }
    }