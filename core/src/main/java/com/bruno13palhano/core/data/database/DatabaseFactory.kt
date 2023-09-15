package com.bruno13palhano.core.data.database

import app.cash.sqldelight.ColumnAdapter
import cache.Product
import cache.SaleTable
import cache.StockOrderTable
import com.bruno13palhano.cache.ShopDatabase

internal class DatabaseFactory(private val driverFactory: DriverFactory) {
    fun createDriver(): ShopDatabase {
        return ShopDatabase(
            driver = driverFactory.createDriver(),
            SaleTableAdapter = SaleTable.Adapter(
                categoriesAdapter = listOfStringAdapter
            ),
            productAdapter = Product.Adapter(
                categoriesAdapter = listOfStringAdapter
            ),
            StockOrderTableAdapter = StockOrderTable.Adapter(
                categoriesAdapter = listOfStringAdapter
            )
        )
    }
}

private val listOfStringAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(",")
        }

    override fun encode(value: List<String>) = value.joinToString(separator = ",")
}