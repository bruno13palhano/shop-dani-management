package com.bruno13palhano.core.sync

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.bruno13palhano.core.sync.works.CategoryWork

object Sync {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SyncWorkName,
                ExistingWorkPolicy.KEEP,
                CategoryWork.startUpSyncWork()
            )
        }
    }
}

internal const val SyncWorkName = "SyncWorkName"