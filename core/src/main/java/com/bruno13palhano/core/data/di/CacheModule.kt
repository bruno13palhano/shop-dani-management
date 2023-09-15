package com.bruno13palhano.core.data.di

import android.content.Context
import cache.CustomerTableQueries
import cache.SaleTableQueries
import cache.ShopDatabaseQueries
import cache.StockOrderTableQueries
import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.database.DatabaseFactory
import com.bruno13palhano.core.data.database.DriverFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CacheModule {

    @Provides
    @Singleton
    fun provideShopDatabaseFactoryDriver(
        @ApplicationContext context: Context
    ): ShopDatabase {
        return DatabaseFactory(
            driverFactory = DriverFactory(context = context)
        ).createDriver()
    }

    @Provides
    @Singleton
    fun provideShop(database: ShopDatabase): ShopDatabaseQueries = database.shopDatabaseQueries

    @Provides
    @Singleton
    fun provideSaleTable(database: ShopDatabase): SaleTableQueries = database.saleTableQueries

    @Provides
    @Singleton
    fun provideCustomerTable(
        database: ShopDatabase
    ): CustomerTableQueries = database.customerTableQueries

    @Provides
    @Singleton
    fun provideStockOrderTable(
        database: ShopDatabase
    ): StockOrderTableQueries = database.stockOrderTableQueries
}