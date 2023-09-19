package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.repository.ProductRepositoryRoom
import com.bruno13palhano.core.data.repository.SaleRepositoryRoom
import com.bruno13palhano.core.data.repository.SearchCacheRepositoryRoom
import com.bruno13palhano.core.data.repository.ProductRepositoryLight
import com.bruno13palhano.core.data.repository.SaleRepositoryLight
import com.bruno13palhano.core.data.repository.SearchCacheRepositoryLight
import com.bruno13palhano.core.data.repository.ShoppingRepositoryLight
import com.bruno13palhano.core.data.repository.ShoppingRepositoryRoom
import com.bruno13palhano.core.data.repository.StockOrderRepositoryLight
import com.bruno13palhano.core.data.repository.StockOrderRepositoryRoom
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.Shopping
import com.bruno13palhano.core.model.StockOrder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Annotation to inject [ProductRepositoryRoom].
 *
 * Injects the default [ProductRepositoryRoom] implementation.
 */
@Qualifier
annotation class DefaultProductRepository

@Qualifier
annotation class SecondaryProductRepository

@Qualifier
annotation class CategoryRep

@Qualifier
annotation class CustomerRep

@Qualifier
annotation class DeliveryRep

/**
 * Annotation to inject [SearchCacheRepositoryRoom]
 *
 * Injects the default [SearchCacheRepositoryRoom] implementation.
 */
@Qualifier
annotation class DefaultSearchCacheRepository

@Qualifier
annotation class SecondarySearchCacheRepository

@Qualifier
annotation class DefaultSaleRepository

@Qualifier
annotation class SecondarySaleRepository

@Qualifier
annotation class DefaultShoppingRepository

@Qualifier
annotation class SecondaryShoppingRepository

@Qualifier
annotation class DefaultStockOrderRepository

@Qualifier
annotation class SecondaryStockOrderRepository

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @DefaultProductRepository
    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: ProductRepositoryRoom): ProductData<Product>

    @DefaultSearchCacheRepository
    @Singleton
    @Binds
    abstract fun bindSearchCacheRepository(
        repository: SearchCacheRepositoryRoom
    ): SearchCacheData<SearchCache>

    @DefaultSaleRepository
    @Singleton
    @Binds
    abstract fun bindSaleRepository(repository: SaleRepositoryRoom): SaleData<Sale>

    @DefaultShoppingRepository
    @Singleton
    @Binds
    abstract fun bindShoppingRepository(
        repository: ShoppingRepositoryRoom
    ): ShoppingData<Shopping>

    @DefaultStockOrderRepository
    @Singleton
    @Binds
    abstract fun bindStockOrderRepository(
        repository: StockOrderRepositoryRoom)
    : StockOrderData<StockOrder>

    @SecondaryProductRepository
    @Singleton
    @Binds
    abstract fun bindSecondaryProductRepository(
        repository: ProductRepositoryLight
    ): ProductData<Product>

    @SecondarySaleRepository
    @Singleton
    @Binds
    abstract fun bindSecondarySaleRepository(repository: SaleRepositoryLight): SaleData<Sale>

    @SecondaryStockOrderRepository
    @Singleton
    @Binds
    abstract fun bindSecondaryStockOrderRepository(
        repository: StockOrderRepositoryLight
    ): StockOrderData<StockOrder>

    @SecondaryShoppingRepository
    @Singleton
    @Binds
    abstract fun bindSecondaryShoppingRepository(
        repository: ShoppingRepositoryLight
    ): ShoppingData<Shopping>

    @SecondarySearchCacheRepository
    @Singleton
    @Binds
    abstract fun bindSecondaryCacheRepository(
        repository: SearchCacheRepositoryLight
    ): SearchCacheData<SearchCache>

    @CategoryRep
    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepository): CategoryData<Category>

    @CustomerRep
    @Singleton
    @Binds
    abstract fun bindCustomerRepository(repository: CustomerRepository): CustomerData<Customer>

    @DeliveryRep
    @Singleton
    @Binds
    abstract fun bindDeliveryRepository(repository: DeliveryRepository): DeliveryData<Delivery>
}