package com.bruno13palhano.core.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.bruno13palhano.cache.ShopDatabase

internal class DriverFactory(private val context: Context) {
    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ShopDatabase.Schema, context, "shopDatabase.db")
    }
}