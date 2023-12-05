package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import javax.inject.Inject

internal class DataVersionNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : VersionNetwork {
    override suspend fun getAll(): List<DataVersion> {
        return try {
            apiService.getVersion().map { it.asExternal() }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            emptyList()
        }
    }

    override suspend fun insert(data: DataVersion) {
        try {
            apiService.insertVersion(version = data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun update(data: DataVersion) {
        try {
            apiService.updateVersion(version = data.asNetwork())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override suspend fun delete(id: Long) {
        try {
            apiService.deleteVersion(id)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
}