package com.bruno13palhano.core.sync

import com.bruno13palhano.core.data.repository.versionNetToVersion
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Model
import com.bruno13palhano.core.network.model.DataVersionNet
import kotlinx.coroutines.CancellationException

interface Synchronizer {
    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

suspend fun <T : Model> Synchronizer.syncData(
    dataVersion: DataVersion,
    networkVersion: DataVersionNet,
    dataList: List<T>,
    networkList: List<T>,
    onPush: suspend (deleteIds: List<Long>, saveList: List<T>, dtVersion: DataVersion) -> Unit,
    onPull: suspend (deleteIds: List<Long>, saveList: List<T>, netVersion: DataVersion) -> Unit
) = suspendRunCatching {
    if (dataVersion.id != 0L || networkVersion.id != 0L) {
        if (dataVersion.timestamp > networkVersion.timestamp) {
            if (dataList.isNotEmpty()) {
                onPush(networkList.map { it.id }, dataList, dataVersion)
            }
        } else if (dataVersion.timestamp < networkVersion.timestamp) {
            if (networkList.isNotEmpty()) {
                onPull(dataList.map { it.id }, networkList, versionNetToVersion(networkVersion))
            }
        }
    }
}.isSuccess

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Result.failure(exception)
}