package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRepository
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

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

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

    @ProductRep
    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: ProductRepository): ProductData<Product>

    @SaleRep
    @Singleton
    @Binds
    abstract fun bindSaleRepository(repository: SaleRepository): SaleData<Sale>

    @SearchCacheRep
    @Singleton
    @Binds
    abstract fun bindSearchCacheRepository(
        repository: SearchCacheRepository
    ): SearchCacheData<SearchCache>

    @StockOrderRep
    @Singleton
    @Binds
    abstract fun bindStockOrderRepository(
        repository: StockOrderRepository
    ): StockOrderData<StockOrder>
}