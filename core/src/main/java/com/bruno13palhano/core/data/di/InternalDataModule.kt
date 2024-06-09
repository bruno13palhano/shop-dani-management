package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.DefaultExcelSheet
import com.bruno13palhano.core.data.repository.ExcelSheet
import com.bruno13palhano.core.data.repository.catalog.CatalogData
import com.bruno13palhano.core.data.repository.catalog.LocalCatalogData
import com.bruno13palhano.core.data.repository.category.CategoryData
import com.bruno13palhano.core.data.repository.category.LocalCategoryData
import com.bruno13palhano.core.data.repository.customer.CustomerData
import com.bruno13palhano.core.data.repository.customer.LocalCustomerData
import com.bruno13palhano.core.data.repository.product.LocalProductData
import com.bruno13palhano.core.data.repository.product.ProductData
import com.bruno13palhano.core.data.repository.sale.LocalSaleData
import com.bruno13palhano.core.data.repository.sale.SaleData
import com.bruno13palhano.core.data.repository.searchcache.LocalSearchCacheData
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheData
import com.bruno13palhano.core.data.repository.stock.LocalStockData
import com.bruno13palhano.core.data.repository.stock.StockData
import com.bruno13palhano.core.data.repository.user.LocalUserData
import com.bruno13palhano.core.data.repository.user.UserData
import com.bruno13palhano.core.data.repository.version.LocalVersionData
import com.bruno13palhano.core.data.repository.version.VersionData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class InternalUser

@Qualifier
internal annotation class InternalCategory

@Qualifier
internal annotation class InternalCustomer

@Qualifier
internal annotation class InternalProduct

@Qualifier
internal annotation class InternalSale

@Qualifier
internal annotation class InternalSearchCache

@Qualifier
internal annotation class InternalStock

@Qualifier
internal annotation class InternalCatalog

@Qualifier
internal annotation class InternalVersion

@Qualifier
internal annotation class InternalDefaultExcelSheet

@InstallIn(SingletonComponent::class)
@Module
internal abstract class InternalDataModule {
    @InternalUser
    @Singleton
    @Binds
    abstract fun bindInternalUser(data: LocalUserData): UserData

    @InternalCategory
    @Singleton
    @Binds
    abstract fun bindInternalCategory(data: LocalCategoryData): CategoryData

    @InternalCustomer
    @Singleton
    @Binds
    abstract fun bindInternalCustomer(data: LocalCustomerData): CustomerData

    @InternalProduct
    @Singleton
    @Binds
    abstract fun bindInternalProduct(data: LocalProductData): ProductData

    @InternalSale
    @Singleton
    @Binds
    abstract fun bindInternalSale(data: LocalSaleData): SaleData

    @InternalSearchCache
    @Singleton
    @Binds
    abstract fun bindInternalSearchCache(data: LocalSearchCacheData): SearchCacheData

    @InternalStock
    @Singleton
    @Binds
    abstract fun bindInternalStock(data: LocalStockData): StockData

    @InternalCatalog
    @Singleton
    @Binds
    abstract fun bindInternalCatalog(data: LocalCatalogData): CatalogData

    @InternalVersion
    @Singleton
    @Binds
    abstract fun bindInternalVersion(data: LocalVersionData): VersionData

    @InternalDefaultExcelSheet
    @Singleton
    @Binds
    abstract fun bindInternalDefaultExcelSheet(data: DefaultExcelSheet): ExcelSheet
}