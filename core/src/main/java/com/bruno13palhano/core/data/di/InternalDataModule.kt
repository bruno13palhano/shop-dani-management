package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.data.repository.customer.CustomerLight
import com.bruno13palhano.core.data.repository.delivery.DeliveryLight
import com.bruno13palhano.core.data.repository.product.ProductLight
import com.bruno13palhano.core.data.repository.sale.SaleLight
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheLight
import com.bruno13palhano.core.data.repository.shopping.ShoppingLight
import com.bruno13palhano.core.data.repository.stockorder.StockOrderLight
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
internal annotation class InternalShoppingLight

@Qualifier
internal annotation class InternalStockOrderLight

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

    @InternalShoppingLight
    @Singleton
    @Binds
    abstract fun bindInternalShoppingLight(repository: ShoppingLight): ShoppingData<Shopping>

    @InternalStockOrderLight
    @Singleton
    @Binds
    abstract fun bindInternalStockOrderLight(repository: StockOrderLight): StockOrderData<StockOrder>
}