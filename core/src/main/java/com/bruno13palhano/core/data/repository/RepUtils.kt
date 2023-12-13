package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Model
import com.bruno13palhano.core.network.access.CrudNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.model.DataVersionNet
import com.bruno13palhano.core.network.model.asNetwork
import kotlinx.coroutines.flow.first

suspend fun getDataVersion(versionData: VersionData, id: Long) =
    try { versionData.getById(id = id).first() }
    catch (ignored: Exception) { DataVersion() }

suspend fun getNetworkVersion(versionNetwork: VersionNetwork, id: Long) =
    try { versionNetwork.getAll().find { it.id == id }?.asNetwork()!! }
    catch (ignored: Exception) { DataVersionNet() }

suspend fun <T : Model> getDataList(data: DataOperations<T>) =
    try { data.getAll().first() }
    catch (ignored: Exception) { emptyList() }

suspend fun <T : Model> getNetworkList(network: CrudNetwork<T>) =
    try { network.getAll() }
    catch (ignored: Exception) { emptyList() }

object Versions {
    fun categoryVersion(timestamp: String) = DataVersion(1L, "CATEGORY", timestamp)
    fun productVersion(timestamp: String) = DataVersion(2L, "PRODUCT", timestamp)
    fun stockVersion(timestamp: String) = DataVersion(3L, "STOCK", timestamp)
    fun saleVersion(timestamp: String) = DataVersion(4L, "SALE", timestamp)
    fun deliveryVersion(timestamp: String) = DataVersion(5L, "DELIVERY", timestamp)
    fun customerVersion(timestamp: String) = DataVersion(6L, "CUSTOMER", timestamp)
    fun catalogVersion(timestamp: String) = DataVersion(7L, "CATALOG", timestamp)
}