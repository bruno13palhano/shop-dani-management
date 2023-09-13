package com.bruno13palhano.core.data.database

import com.bruno13palhano.cache.ShopDatabase

internal class DatabaseFactory(private val driverFactory: DriverFactory) {
    fun createDriver(): ShopDatabase {
        return ShopDatabase(driver = driverFactory.createDriver())
    }
}