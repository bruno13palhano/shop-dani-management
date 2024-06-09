package com.bruno13palhano.core.sync

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.bruno13palhano.core.sync.works.FetchDataWork

object Sync {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                FetchDataWork.startUpSyncWork()
            )
        }
    }
}

internal const val SYNC_WORK_NAME = "SyncWorkName"