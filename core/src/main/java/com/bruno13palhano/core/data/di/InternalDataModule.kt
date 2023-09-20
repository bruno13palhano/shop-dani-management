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
import com.bruno13palhano.core.data.repository.category.CategoryRoom
import com.bruno13palhano.core.data.repository.customer.CustomerLight
import com.bruno13palhano.core.data.repository.customer.CustomerRoom
import com.bruno13palhano.core.data.repository.delivery.DeliveryLight
import com.bruno13palhano.core.data.repository.delivery.DeliveryRoom
import com.bruno13palhano.core.data.repository.product.ProductLight
import com.bruno13palhano.core.data.repository.product.ProductRoom
import com.bruno13palhano.core.data.repository.sale.SaleLight
import com.bruno13palhano.core.data.repository.sale.SaleRoom
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheLight
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRoom
import com.bruno13palhano.core.data.repository.shopping.ShoppingLight
import com.bruno13palhano.core.data.repository.shopping.ShoppingRoom
import com.bruno13palhano.core.data.repository.stockorder.StockOrderLight
import com.bruno13palhano.core.data.repository.stockorder.StockOrderRoom
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
internal annotation class InternalCategoryRoom

@Qualifier
internal annotation class InternalCategoryLight

@Qualifier
internal annotation class InternalCustomerRoom

@Qualifier
internal annotation class InternalCustomerLight

@Qualifier
internal annotation class InternalDeliveryRoom

@Qualifier
internal annotation class InternalDeliveryLight

@Qualifier
internal annotation class InternalProductRoom

@Qualifier
internal annotation class InternalProductLight

@Qualifier
internal annotation class InternalSaleRoom

@Qualifier
internal annotation class InternalSaleLight

@Qualifier
internal annotation class InternalSearchCacheRoom

@Qualifier
internal annotation class InternalSearchCacheLight

@Qualifier
internal annotation class InternalShoppingRoom

@Qualifier
internal annotation class InternalShoppingLight

@Qualifier
internal annotation class InternalStockOrderRoom

@Qualifier
internal annotation class InternalStockOrderLight

@InstallIn(SingletonComponent::class)
@Module
internal abstract class InternalDataModule {

    @InternalCategoryRoom
    @Singleton
    @Binds
    abstract fun bindInternalCategoryRoom(repository: CategoryRoom): CategoryData<Category>

    @InternalCategoryLight
    @Singleton
    @Binds
    abstract fun bindInternalCategoryLight(repository: CategoryLight): CategoryData<Category>

    @InternalCustomerRoom
    @Singleton
    @Binds
    abstract fun bindInternalCustomerRoom(repository: CustomerRoom): CustomerData<Customer>

    @InternalCustomerLight
    @Singleton
    @Binds
    abstract fun bindInternalCustomerLight(repository: CustomerLight): CustomerData<Customer>

    @InternalDeliveryRoom
    @Singleton
    @Binds
    abstract fun bindInternalDeliveryRoom(repository: DeliveryRoom): DeliveryData<Delivery>

    @InternalDeliveryLight
    @Singleton
    @Binds
    abstract fun bindInternalDeliveryLight(repository: DeliveryLight): DeliveryData<Delivery>

    @InternalProductRoom
    @Singleton
    @Binds
    abstract fun bindInternalProductRoom(repository: ProductRoom): ProductData<Product>

    @InternalProductLight
    @Singleton
    @Binds
    abstract fun bindInternalProductLight(repository: ProductLight): ProductData<Product>

    @InternalSaleRoom
    @Singleton
    @Binds
    abstract fun bindInternalSaleRoom(repository: SaleRoom): SaleData<Sale>

    @InternalSaleLight
    @Singleton
    @Binds
    abstract fun bindInternalSaleLight(repository: SaleLight): SaleData<Sale>

    @InternalSearchCacheRoom
    @Singleton
    @Binds
    abstract fun bindInternalSearchCacheRoom(
        repository: SearchCacheRoom
    ): SearchCacheData<SearchCache>

    @InternalSearchCacheLight
    @Singleton
    @Binds
    abstract fun bindInternalSearchCacheLight(
        repository: SearchCacheLight
    ): SearchCacheData<SearchCache>

    @InternalShoppingRoom
    @Singleton
    @Binds
    abstract fun bindInternalShoppingRoom(repository: ShoppingRoom): ShoppingData<Shopping>

    @InternalShoppingLight
    @Singleton
    @Binds
    abstract fun bindInternalShoppingLight(repository: ShoppingLight): ShoppingData<Shopping>

    @InternalStockOrderRoom
    @Singleton
    @Binds
    abstract fun bindStockOrderRepository(repository: StockOrderRoom): StockOrderData<StockOrder>

    @InternalStockOrderLight
    @Singleton
    @Binds
    abstract fun bindInternalStockOrderLight(repository: StockOrderLight): StockOrderData<StockOrder>
}