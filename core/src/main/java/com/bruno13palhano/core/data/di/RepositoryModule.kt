package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.domain.CustomerInfoUseCase
import com.bruno13palhano.core.data.domain.FinancialUseCase
import com.bruno13palhano.core.data.domain.SaleInfoUseCase
import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.data.repository.catalog.DefaultCatalogRepository
import com.bruno13palhano.core.data.repository.category.DefaultCategoryRepository
import com.bruno13palhano.core.data.repository.customer.DefaultCustomerRepository
import com.bruno13palhano.core.data.repository.product.DefaultProductRepository
import com.bruno13palhano.core.data.repository.sale.DefaultSaleRepository
import com.bruno13palhano.core.data.repository.searchcache.DefaultSearchCacheRepository
import com.bruno13palhano.core.data.repository.stock.DefaultStockRepository
import com.bruno13palhano.core.data.repository.user.DefaultUserRepository
import com.bruno13palhano.core.data.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class UserRep

@Qualifier
annotation class CategoryRep

@Qualifier
annotation class CustomerRep

@Qualifier
annotation class ProductRep

@Qualifier
annotation class SaleRep

@Qualifier
annotation class SearchCacheRep

@Qualifier
annotation class StockRep

@Qualifier
annotation class CatalogRep

@Qualifier
annotation class Financial

@Qualifier
annotation class SalesInformation

@Qualifier
annotation class CustomerInformation

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @UserRep
    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: DefaultUserRepository): UserRepository

    @CategoryRep
    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: DefaultCategoryRepository): CategoryRepository

    @CustomerRep
    @Singleton
    @Binds
    abstract fun bindCustomerRepository(repository: DefaultCustomerRepository): CustomerRepository

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
    abstract fun bindSearchCacheRepository(
        repository: DefaultSearchCacheRepository
    ): SearchCacheRepository

    @StockRep
    @Singleton
    @Binds
    abstract fun bindStockRepository(repository: DefaultStockRepository): StockRepository

    @CatalogRep
    @Singleton
    @Binds
    abstract fun bindCatalogRepository(repository: DefaultCatalogRepository): CatalogRepository

    @Financial
    @Singleton
    @Binds
    abstract fun bindFinancialUseCase(financial: FinancialUseCase): FinancialUseCase

    @SalesInformation
    @Singleton
    @Binds
    abstract fun bindSaleInfoUseCase(salesInformation: SaleInfoUseCase): SaleInfoUseCase

    @CustomerInformation
    @Singleton
    @Binds
    abstract fun bindCustomerInfoUseCase(
        customerInformation: CustomerInfoUseCase
    ): CustomerInfoUseCase
}