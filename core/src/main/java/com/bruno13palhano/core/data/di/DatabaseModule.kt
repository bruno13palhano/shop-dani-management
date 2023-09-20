package com.bruno13palhano.core.data.di

import android.content.Context
import cache.CategoryTableQueries
import cache.CustomerTableQueries
import cache.DeliveryTableQueries
import cache.ProductCategoriesTableQueries
import cache.SaleTableQueries
import cache.SearchCacheTableQueries
import cache.ShopDatabaseQueries
import cache.ShoppingTableQueries
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

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {

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

    @Provides
    @Singleton
    fun provideCategoryTable(
        database: ShopDatabase
    ): CategoryTableQueries = database.categoryTableQueries

    @Provides
    @Singleton
    fun provideShoppingTable(
        database: ShopDatabase
    ): ShoppingTableQueries = database.shoppingTableQueries

    @Provides
    @Singleton
    fun provideSearchCacheTable(
        database: ShopDatabase
    ): SearchCacheTableQueries = database.searchCacheTableQueries

    @Provides
    @Singleton
    fun provideDeliveryTable(
        database: ShopDatabase
    ): DeliveryTableQueries = database.deliveryTableQueries

    @Provides
    @Singleton
    fun provideProductCategoriesTable(
        database: ShopDatabase
    ): ProductCategoriesTableQueries = database.productCategoriesTableQueries
}