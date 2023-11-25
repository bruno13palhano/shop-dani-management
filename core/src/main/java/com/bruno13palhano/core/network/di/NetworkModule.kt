package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.core.network.CrudNetwork
import com.bruno13palhano.core.network.access.CatalogNetworkRetrofit
import com.bruno13palhano.core.network.access.CategoryNetworkRetrofit
import com.bruno13palhano.core.network.access.CustomerNetworkRetrofit
import com.bruno13palhano.core.network.access.DeliveryNetworkRetrofit
import com.bruno13palhano.core.network.access.ProductNetworkRetrofit
import com.bruno13palhano.core.network.access.SaleNetworkRetrofit
import com.bruno13palhano.core.network.access.StockOrderNetworkRetrofit
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class DefaultCatalogNet

@Qualifier
internal annotation class DefaultCategoryNet

@Qualifier
internal annotation class DefaultCustomerNet

@Qualifier
internal annotation class DefaultDeliveryNet

@Qualifier
internal annotation class DefaultProductNet

@Qualifier
internal annotation class DefaultSaleNet

@Qualifier
internal annotation class DefaultStockOrderNet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @DefaultCatalogNet
    @Singleton
    @Binds
    abstract fun bindCatalogNetwork(catalogNetwork: CatalogNetworkRetrofit): CrudNetwork<Catalog>

    @DefaultCategoryNet
    @Singleton
    @Binds
    abstract fun bindCategoryNetwork(
        categoryNetwork: CategoryNetworkRetrofit
    ): CrudNetwork<Category>

    @DefaultCustomerNet
    @Singleton
    @Binds
    abstract fun bindCustomerNetwork(
        customerNetwork: CustomerNetworkRetrofit
    ): CrudNetwork<Customer>

    @DefaultDeliveryNet
    @Singleton
    @Binds
    abstract fun bindDeliveryNetwork(
        deliveryNetwork: DeliveryNetworkRetrofit
    ): CrudNetwork<Delivery>

    @DefaultProductNet
    @Singleton
    @Binds
    abstract fun bindProductNetwork(productNetwork: ProductNetworkRetrofit): CrudNetwork<Product>

    @DefaultSaleNet
    @Singleton
    @Binds
    abstract fun bindSaleNetwork(saleNetwork: SaleNetworkRetrofit): CrudNetwork<Sale>

    @DefaultStockOrderNet
    @Singleton
    @Binds
    abstract fun bindStockOrderNetwork(
        stockOrderNetwork: StockOrderNetworkRetrofit
    ): CrudNetwork<StockOrder>
}