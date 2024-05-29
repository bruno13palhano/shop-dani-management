package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.network.access.CatalogNetwork
import com.bruno13palhano.core.network.access.CategoryNetwork
import com.bruno13palhano.core.network.access.CustomerNetwork
import com.bruno13palhano.core.network.access.ProductNetwork
import com.bruno13palhano.core.network.access.SaleNetwork
import com.bruno13palhano.core.network.access.StockNetwork
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.access.VersionNetwork
import com.bruno13palhano.core.network.access.impl.CatalogNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.CategoryNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.CustomerNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.DataVersionNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.ProductNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.SaleNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.StockNetworkRetrofit
import com.bruno13palhano.core.network.access.impl.UserNetworkRetrofit
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class DefaultUserNet

@Qualifier
internal annotation class DefaultCatalogNet

@Qualifier
internal annotation class DefaultCategoryNet

@Qualifier
internal annotation class DefaultCustomerNet

@Qualifier
internal annotation class DefaultProductNet

@Qualifier
internal annotation class DefaultSaleNet

@Qualifier
internal annotation class DefaultStockNet

@Qualifier
internal annotation class DefaultVersionNet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @DefaultUserNet
    @Singleton
    @Binds
    abstract fun bindUserNetwork(network: UserNetworkRetrofit): UserNetwork

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

    @DefaultProductNet
    @Singleton
    @Binds
    abstract fun bindProductNetwork(network: ProductNetworkRetrofit): ProductNetwork

    @DefaultSaleNet
    @Singleton
    @Binds
    abstract fun bindSaleNetwork(network: SaleNetworkRetrofit): SaleNetwork

    @DefaultStockNet
    @Singleton
    @Binds
    abstract fun bindStockNetwork(network: StockNetworkRetrofit): StockNetwork

    @DefaultVersionNet
    @Singleton
    @Binds
    abstract fun bindVersionNetwork(network: DataVersionNetworkRetrofit): VersionNetwork
}
