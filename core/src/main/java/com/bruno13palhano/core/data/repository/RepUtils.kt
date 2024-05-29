package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.repository.version.VersionData
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Model
import com.bruno13palhano.core.network.access.CrudNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.model.DataVersionNet
import kotlinx.coroutines.flow.first

suspend fun getDataVersion(
    versionData: VersionData,
    id: Long,
) = try {
    versionData.getById(id = id).first()
} catch (ignored: Exception) {
    DataVersion()
}

suspend fun getNetworkVersion(
    versionNetwork: VersionNetwork,
    id: Long,
) = try {
    versionNetwork.getAll().find { it.id == id }!!
} catch (ignored: Exception) {
    DataVersionNet()
}

suspend fun <T : Model> getDataList(data: DataOperations<T>) =
    try {
        data.getAll().first()
    } catch (ignored: Exception) {
        emptyList()
    }

suspend fun <T : Model> getNetworkList(network: CrudNetwork<T>) =
    try {
        network.getAll()
    } catch (ignored: Exception) {
        emptyList()
    }

object Versions {
    const val CATEGORY_VERSION_ID = 1L
    const val PRODUCT_VERSION_ID = 2L
    const val STOCK_VERSION_ID = 3L
    const val SALE_VERSION_ID = 4L
    const val CUSTOMER_VERSION_ID = 5L
    const val CATALOG_VERSION_ID = 6L
    private const val CATEGORY_NAME = "CATEGORY"
    private const val PRODUCT_NAME = "PRODUCT"
    private const val STOCK_NAME = "STOCK"
    private const val SALE_NAME = "SALE"
    private const val CUSTOMER_NAME = "CUSTOMER"
    private const val CATALOG_NAME = "CATALOG"

    fun categoryVersion(timestamp: String) =
        DataVersion(
            id = CATEGORY_VERSION_ID,
            name = CATEGORY_NAME,
            timestamp = timestamp,
        )

    fun productVersion(timestamp: String) =
        DataVersion(
            id = PRODUCT_VERSION_ID,
            name = PRODUCT_NAME,
            timestamp = timestamp,
        )

    fun stockVersion(timestamp: String) =
        DataVersion(
            id = STOCK_VERSION_ID,
            name = STOCK_NAME,
            timestamp = timestamp,
        )

    fun saleVersion(timestamp: String) =
        DataVersion(
            id = SALE_VERSION_ID,
            name = SALE_NAME,
            timestamp = timestamp,
        )

    fun customerVersion(timestamp: String) =
        DataVersion(
            id = CUSTOMER_VERSION_ID,
            name = CUSTOMER_NAME,
            timestamp = timestamp,
        )

    fun catalogVersion(timestamp: String) =
        DataVersion(
            id = CATALOG_VERSION_ID,
            name = CATALOG_NAME,
            timestamp = timestamp,
        )
}
