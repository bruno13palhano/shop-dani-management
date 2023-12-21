package com.bruno13palhano.core.data.di

import android.content.Context
import cache.CatalogTableQueries
import cache.CategoryTableQueries
import cache.CustomerTableQueries
import cache.ProductCategoriesTableQueries
import cache.SaleTableQueries
import cache.SearchCacheTableQueries
import cache.ShopDatabaseQueries
import cache.StockTableQueries
import cache.UsersTableQueries
import cache.VersionTableQueries
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
    fun provideUsersTable(database: ShopDatabase): UsersTableQueries = database.usersTableQueries

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
    fun provideStockTable(
        database: ShopDatabase
    ): StockTableQueries = database.stockTableQueries

    @Provides
    @Singleton
    fun provideCategoryTable(
        database: ShopDatabase
    ): CategoryTableQueries = database.categoryTableQueries

    @Provides
    @Singleton
    fun provideSearchCacheTable(
        database: ShopDatabase
    ): SearchCacheTableQueries = database.searchCacheTableQueries

    @Provides
    @Singleton
    fun provideProductCategoriesTable(
        database: ShopDatabase
    ): ProductCategoriesTableQueries = database.productCategoriesTableQueries

    @Provides
    @Singleton
    fun provideCatalogTable(
        database: ShopDatabase
    ): CatalogTableQueries = database.catalogTableQueries

    @Provides
    @Singleton
    fun provideVersionTable(
        database: ShopDatabase
    ): VersionTableQueries = database.versionTableQueries
}