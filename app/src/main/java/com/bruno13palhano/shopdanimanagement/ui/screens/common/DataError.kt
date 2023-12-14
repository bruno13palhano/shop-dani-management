package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bruno13palhano.shopdanimanagement.R

sealed class DataError(val error: Int, @StringRes val resourceId: Int) {
    object InsertDatabase: DataError(1, R.string.insert_database_error)
    object UpdateDatabase: DataError(2, R.string.update_database_error)
    object DeleteDatabase: DataError(3, R.string.delete_database_error)
    object InsertServer: DataError(4, R.string.insert_server_error)
    object UpdateServer: DataError(5, R.string.update_server_error)
    object DeleteServer: DataError(6, R.string.delete_server_error)
}

@Composable
fun getErrors() = listOf(
    stringResource(id = DataError.InsertDatabase.resourceId),
    stringResource(id = DataError.UpdateDatabase.resourceId),
    stringResource(id = DataError.DeleteDatabase.resourceId),
    stringResource(id = DataError.InsertServer.resourceId),
    stringResource(id = DataError.UpdateServer.resourceId),
    stringResource(id = DataError.DeleteServer.resourceId)
)