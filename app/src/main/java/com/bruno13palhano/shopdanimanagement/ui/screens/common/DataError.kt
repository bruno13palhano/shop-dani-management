package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.annotation.StringRes
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.shopdanimanagement.R

sealed class DataError(val error: Int, @StringRes val resourceId: Int) {
    object InsertDatabase: DataError(
        error = Errors.INSERT_DATABASE_ERROR,
        resourceId = R.string.insert_database_error
    )
    object UpdateDatabase: DataError(
        error = Errors.UPDATE_DATABASE_ERROR,
        resourceId = R.string.update_database_error
    )
    object DeleteDatabase: DataError(
        error = Errors.DELETE_DATABASE_ERROR,
        resourceId = R.string.delete_database_error
    )
    object InsertServer: DataError(
        error = Errors.INSERT_SERVER_ERROR,
        resourceId = R.string.insert_server_error
    )
    object UpdateServer: DataError(
        error = Errors.UPDATE_SERVER_ERROR,
        resourceId = R.string.update_server_error
    )
    object DeleteServer: DataError(
        error = Errors.DELETE_SERVER_ERROR,
        resourceId = R.string.delete_server_error
    )
    object LoginDatabase: DataError(
        error = Errors.LOGIN_DATABASE_ERROR,
        resourceId = R.string.login_database_error
    )
    object LoginServer: DataError(
        error = Errors.LOGIN_SERVER_ERROR,
        resourceId = R.string.login_server_error
    )
    object FillMissingFields: DataError(
        error = Errors.FILL_MISSING_FIELDS,
        resourceId = R.string.empty_fields_error
    )
    object InsufficientItemsStock: DataError(
        error = Errors.INSUFFICIENT_ITEMS_STOCK,
        resourceId = R.string.insufficient_items_stock
    )
}