package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
import com.bruno13palhano.core.data.repository.catalog.DefaultCatalogRepository
import com.bruno13palhano.core.data.repository.category.DefaultCategoryRepository
import com.bruno13palhano.core.data.repository.customer.DefaultCustomerRepository
import com.bruno13palhano.core.data.repository.delivery.DefaultDeliveryRepository
import com.bruno13palhano.core.data.repository.product.DefaultProductRepository
import com.bruno13palhano.core.data.repository.sale.DefaultSaleRepository
import com.bruno13palhano.core.data.repository.searchcache.DefaultSearchCacheRepository
import com.bruno13palhano.core.data.repository.stockorder.DefaultStockOrderRepository
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
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
annotation class CategoryRep

@Qualifier
annotation class CustomerRep

@Qualifier
annotation class DeliveryRep

@Qualifier
annotation class ProductRep

@Qualifier
annotation class SaleRep

@Qualifier
annotation class SearchCacheRep

@Qualifier
annotation class StockOrderRep

@Qualifier
annotation class CatalogRep

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @CategoryRep
    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: DefaultCategoryRepository): CategoryRepository

    @CustomerRep
    @Singleton
    @Binds
    abstract fun bindCustomerRepository(repository: DefaultCustomerRepository): CustomerRepository

    @DeliveryRep
    @Singleton
    @Binds
    abstract fun bindDeliveryRepository(repository: DefaultDeliveryRepository): DeliveryRepository

    @ProductRep
    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: DefaultProductRepository): ProductRepository

    @SaleRep
    @Singleton
    @Binds
    abstract fun bindSaleRepository(repository: DefaultSaleRepository): SaleRepository

    @SearchCacheRep
    @Singleton
    @Binds
    abstract fun bindSearchCacheRepository(repository: DefaultSearchCacheRepository): SearchCacheRepository

    @StockOrderRep
    @Singleton
    @Binds
    abstract fun bindStockOrderRepository(repository: DefaultStockOrderRepository): StockOrderRepository

    @CatalogRep
    @Singleton
    @Binds
    abstract fun bindCatalogRepository(repository: DefaultCatalogRepository): CatalogRepository
}