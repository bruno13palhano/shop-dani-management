package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.VersionData
import com.bruno13palhano.core.data.repository.catalog.CatalogLight
import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.data.repository.customer.CustomerLight
import com.bruno13palhano.core.data.repository.delivery.DeliveryLight
import com.bruno13palhano.core.data.repository.product.ProductLight
import com.bruno13palhano.core.data.repository.sale.SaleLight
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheLight
import com.bruno13palhano.core.data.repository.stockorder.StockOrderLight
import com.bruno13palhano.core.data.repository.version.VersionLight
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.StockOrder
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
    abstract fun bindInternalCategoryLight(repository: CategoryLight): CategoryData<Category>

    @InternalCustomerLight
    @Singleton
    @Binds
    abstract fun bindInternalCustomerLight(repository: CustomerLight): CustomerData<Customer>

    @InternalDeliveryLight
    @Singleton
    @Binds
    abstract fun bindInternalDeliveryLight(repository: DeliveryLight): DeliveryData<Delivery>

    @InternalProductLight
    @Singleton
    @Binds
    abstract fun bindInternalProductLight(repository: ProductLight): ProductData<Product>

    @InternalSaleLight
    @Singleton
    @Binds
    abstract fun bindInternalSaleLight(repository: SaleLight): SaleData<Sale>

    @InternalSearchCacheLight
    @Singleton
    @Binds
    abstract fun bindInternalSearchCacheLight(
        repository: SearchCacheLight
    ): SearchCacheData<SearchCache>

    @InternalStockOrderLight
    @Singleton
    @Binds
    abstract fun bindInternalStockOrderLight(
        repository: StockOrderLight
    ): StockOrderData<StockOrder>

    @InternalCatalogLight
    @Singleton
    @Binds
    abstract fun bindInternalCatalogLight(
        repository: CatalogLight
    ): CatalogData<Catalog>

    @InternalVersionLight
    @Singleton
    @Binds
    abstract fun bindInternalVersionLight(
        repository: VersionLight
    ): VersionData<DataVersion>
}