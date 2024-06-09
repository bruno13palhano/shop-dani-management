package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.RemoteVersionData
import com.bruno13palhano.core.network.model.DataVersionNet
import javax.inject.Inject

internal class RemoteVersionRetrofit
    @Inject
    constructor(
        private val apiService: Service
    ) : RemoteVersionData {
        override suspend fun getAll(): List<DataVersionNet> {
            return try {
                apiService.getVersion()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                emptyList()
            }
        }

        override suspend fun insert(data: DataVersionNet) = apiService.insertVersion(version = data)

        override suspend fun update(data: DataVersionNet) = apiService.updateVersion(version = data)

        override suspend fun delete(id: Long) = apiService.deleteVersion(id)
    }