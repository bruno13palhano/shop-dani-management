package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.access.CatalogNetworkRetrofit
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.access.CategoryNetworkRetrofit
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.access.CustomerNetworkRetrofit
import com.bruno13palhano.core.network.access.DeliveryNetwork
import com.bruno13palhano.core.network.access.DeliveryNetworkRetrofit
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.access.ProductNetworkRetrofit
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.access.SaleNetworkRetrofit
import com.bruno13palhano.core.network.access.StockOrderNetwork
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
    abstract fun bindCatalogNetwork(network: CatalogNetworkRetrofit): CatalogNetwork

    @DefaultCategoryNet
    @Singleton
    @Binds
    abstract fun bindCategoryNetwork(network: CategoryNetworkRetrofit): CategoryNetwork

    @DefaultCustomerNet
    @Singleton
    @Binds
    abstract fun bindCustomerNetwork(network: CustomerNetworkRetrofit): CustomerNetwork

    @DefaultDeliveryNet
    @Singleton
    @Binds
    abstract fun bindDeliveryNetwork(network: DeliveryNetworkRetrofit): DeliveryNetwork

    @DefaultProductNet
    @Singleton
    @Binds
    abstract fun bindProductNetwork(network: ProductNetworkRetrofit): ProductNetwork

    @DefaultSaleNet
    @Singleton
    @Binds
    abstract fun bindSaleNetwork(network: SaleNetworkRetrofit): SaleNetwork

    @DefaultStockOrderNet
    @Singleton
    @Binds
    abstract fun bindStockOrderNetwork(network: StockOrderNetworkRetrofit): StockOrderNetwork
}