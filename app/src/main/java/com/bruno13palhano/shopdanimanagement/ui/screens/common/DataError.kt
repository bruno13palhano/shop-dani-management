package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.annotation.StringRes
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.shopdanimanagement.R

sealed class DataError(val error: Int, @StringRes val resourceId: Int) {
    data object InsertDatabase: DataError(
        error = Errors.INSERT_DATABASE_ERROR,
        resourceId = R.string.insert_database_error
    )
    data object UpdateDatabase: DataError(
        error = Errors.UPDATE_DATABASE_ERROR,
        resourceId = R.string.update_database_error
    )
    data object DeleteDatabase: DataError(
        error = Errors.DELETE_DATABASE_ERROR,
        resourceId = R.string.delete_database_error
    )
    data object InsertServer: DataError(
        error = Errors.INSERT_SERVER_ERROR,
        resourceId = R.string.insert_server_error
    )
    data object UpdateServer: DataError(
        error = Errors.UPDATE_SERVER_ERROR,
        resourceId = R.string.update_server_error
    )
    data object DeleteServer: DataError(
        error = Errors.DELETE_SERVER_ERROR,
        resourceId = R.string.delete_server_error
    )
    data object LoginDatabase: DataError(
        error = Errors.LOGIN_DATABASE_ERROR,
        resourceId = R.string.login_database_error
    )
    data object LoginServer: DataError(
        error = Errors.LOGIN_SERVER_ERROR,
        resourceId = R.string.login_server_error
    )
    data object FillMissingFields: DataError(
        error = Errors.FILL_MISSING_FIELDS,
        resourceId = R.string.empty_fields_error
    )
    data object InsufficientItemsStock: DataError(
        error = Errors.INSUFFICIENT_ITEMS_STOCK,
        resourceId = R.string.insufficient_items_stock
    )
}