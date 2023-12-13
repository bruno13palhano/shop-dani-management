package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.catalog.CatalogData
import com.bruno13palhano.core.data.repository.catalog.DefaultCatalogData
import com.bruno13palhano.core.data.repository.category.CategoryData
import com.bruno13palhano.core.data.repository.category.DefaultCategoryData
import com.bruno13palhano.core.data.repository.customer.CustomerData
import com.bruno13palhano.core.data.repository.customer.DefaultCustomerData
import com.bruno13palhano.core.data.repository.product.DefaultProductData
import com.bruno13palhano.core.data.repository.product.ProductData
import com.bruno13palhano.core.data.repository.sale.DefaultSaleData
import com.bruno13palhano.core.data.repository.sale.SaleData
import com.bruno13palhano.core.data.repository.searchcache.DefaultSearchCacheData
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheData
import com.bruno13palhano.core.data.repository.stockorder.DefaultStockData
import com.bruno13palhano.core.data.repository.stockorder.StockData
import com.bruno13palhano.core.data.repository.version.DefaultVersionData
import com.bruno13palhano.core.data.repository.version.VersionData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class InternalCategoryLight

@Qualifier
internal annotation class InternalCustomerLight

@Qualifier
internal annotation class InternalDeliveryLight

@Qualifier
internal annotation class InternalProductLight

@Qualifier
internal annotation class InternalSaleLight

@Qualifier
internal annotation class InternalSearchCacheLight

@Qualifier
internal annotation class InternalStockOrderLight

@Qualifier
internal annotation class InternalCatalogLight

@Qualifier
internal annotation class InternalVersionLight

@InstallIn(SingletonComponent::class)
@Module
internal abstract class InternalDataModule {

    @InternalCategoryLight
    @Singleton
    @Binds
    abstract fun bindInternalCategoryData(data: DefaultCategoryData): CategoryData

    @InternalCustomerLight
    @Singleton
    @Binds
    abstract fun bindInternalCustomerData(data: DefaultCustomerData): CustomerData

    @InternalProductLight
    @Singleton
    @Binds
    abstract fun bindInternalProductData(data: DefaultProductData): ProductData

    @InternalSaleLight
    @Singleton
    @Binds
    abstract fun bindInternalSaleData(data: DefaultSaleData): SaleData

    @InternalSearchCacheLight
    @Singleton
    @Binds
    abstract fun bindInternalSearchCacheData(data: DefaultSearchCacheData): SearchCacheData

    @InternalStockOrderLight
    @Singleton
    @Binds
    abstract fun bindInternalStockOrderData(data: DefaultStockData): StockData

    @InternalCatalogLight
    @Singleton
    @Binds
    abstract fun bindInternalCatalogData(data: DefaultCatalogData): CatalogData

    @InternalVersionLight
    @Singleton
    @Binds
    abstract fun bindInternalVersionData(data: DefaultVersionData): VersionData
}