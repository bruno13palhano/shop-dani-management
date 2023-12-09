package com.bruno13palhano.core.data.database

import app.cash.sqldelight.ColumnAdapter
import cache.ProductCategoriesTable
import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.model.Category

internal class DatabaseFactory(private val driverFactory: DriverFactory) {
    fun createDriver(): ShopDatabase {
        return ShopDatabase(
            driver = driverFactory.createDriver(),
            ProductCategoriesTableAdapter = ProductCategoriesTable.Adapter(
                categoriesAdapter = listOfCategoryAdapter
            )
        )
    }
}

private val listOfCategoryAdapter = object : ColumnAdapter<List<Category>, String> {
    override fun decode(databaseValue: String): List<Category> =
        if (databaseValue.isEmpty()) {
            emptyList()
        } else {
            databaseValue.split(",").map {
                val params = it.split("&")
                Category(
                    id = params[0].toLong(),
                    category = params[1],
                    timestamp = params[2]
                )
            }
        }

    override fun encode(value: List<Category>) =
        value.joinToString(",") { "${it.id}&${it.category}" + "&${it.timestamp}" }
}