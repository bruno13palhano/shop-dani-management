package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.repository.CategoryRepository
import com.bruno13palhano.core.data.repository.ProductRepository
import com.bruno13palhano.core.data.repository.SaleRepository
import com.bruno13palhano.core.data.repository.SearchCacheRepository
import com.bruno13palhano.core.data.repository.ShoppingCacheRepository
import com.bruno13palhano.core.data.repository.StockOrderRepository
import com.bruno13palhano.core.model.Category
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
 * Annotation to inject [ProductRepository].
 *
 * Injects the default [ProductRepository] implementation.
 */
@Qualifier
annotation class DefaultProductRepository

/**
 * Annotation to inject [CategoryRepository]
 *
 * Injects the default [CategoryRepository] implementation.
 */
@Qualifier
annotation class DefaultCategoryRepository

/**
 * Annotation to inject [SearchCacheRepository]
 *
 * Injects the default [SearchCacheRepository] implementation.
 */
@Qualifier
annotation class DefaultSearchCacheRepository

@Qualifier
annotation class DefaultSaleRepository

@Qualifier
annotation class DefaultShoppingRepository

@Qualifier
annotation class DefaultStockOrderRepository

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @DefaultProductRepository
    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: ProductRepository): ProductData<Product>

    @DefaultCategoryRepository
    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepository): CategoryData<Category>

    @DefaultSearchCacheRepository
    @Singleton
    @Binds
    abstract fun bindSearchCacheRepository(repository: SearchCacheRepository): SearchCacheData<SearchCache>

    @DefaultSaleRepository
    @Singleton
    @Binds
    abstract fun bindSaleRepository(repository: SaleRepository): SaleData<Sale>

    @DefaultShoppingRepository
    @Singleton
    @Binds
    abstract fun bindShoppingRepository(repository: ShoppingCacheRepository): ShoppingData<Shopping>

    @DefaultStockOrderRepository
    @Singleton
    @Binds
    abstract fun bindStockOrderRepository(repository: StockOrderRepository): StockOrderData<StockOrder>
}