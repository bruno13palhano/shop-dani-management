package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Model
import com.bruno13palhano.core.network.access.CrudNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.model.DataVersionNet
import kotlinx.coroutines.flow.first

suspend fun getDataVersion(versionData: VersionData, id: Long) =
    try { versionData.getById(id = id).first() }
    catch (ignored: Exception) { DataVersion() }

suspend fun getNetworkVersion(versionNetwork: VersionNetwork, id: Long) =
    try { versionNetwork.getAll().find { it.id == id }!! }
    catch (ignored: Exception) { DataVersionNet() }

suspend fun <T : Model> getDataList(data: DataOperations<T>) =
    try { data.getAll().first() }
    catch (ignored: Exception) { emptyList() }

suspend fun <T : Model> getNetworkList(network: CrudNetwork<T>) =
    try { network.getAll() }
    catch (ignored: Exception) { emptyList() }

object Versions {
    const val categoryVersionId = 1L
    const val productVersionId = 2L
    const val stockVersionId = 3L
    const val saleVersionId = 4L
    const val customerVersionId = 5L
    const val catalogVersionId = 6L
    private const val categoryName = "CATEGORY"
    private const val productName = "PRODUCT"
    private const val stockName = "STOCK"
    private const val saleName = "SALE"
    private const val customerName = "CUSTOMER"
    private const val catalogName = "CATALOG"

    fun categoryVersion(timestamp: String) = DataVersion(
        id = categoryVersionId,
        name = categoryName,
        timestamp = timestamp
    )

    fun productVersion(timestamp: String) = DataVersion(
        id = productVersionId,
        name = productName,
        timestamp = timestamp
    )

    fun stockVersion(timestamp: String) = DataVersion(
        id = stockVersionId,
        name = stockName,
        timestamp = timestamp
    )

    fun saleVersion(timestamp: String) = DataVersion(
        id = saleVersionId,
        name = saleName,
        timestamp = timestamp
    )

    fun customerVersion(timestamp: String) = DataVersion(
        id = customerVersionId,
        name = customerName,
        timestamp = timestamp
    )

    fun catalogVersion(timestamp: String) = DataVersion(
        id = catalogVersionId,
        name = catalogName,
        timestamp = timestamp
    )
}