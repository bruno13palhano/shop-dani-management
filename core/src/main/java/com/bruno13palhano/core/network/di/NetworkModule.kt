package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.network.category.CategoryNetwork
import com.bruno13palhano.core.network.category.CategoryNetworkRetrofit
import com.bruno13palhano.core.network.product.ProductNetwork
import com.bruno13palhano.core.network.product.ProductNetworkRetrofit
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class CategoryNet

@Qualifier
internal annotation class ProductNet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @CategoryNet
    @Singleton
    @Binds
    abstract fun bindCategoryNetwork(categoryNetwork: CategoryNetworkRetrofit): CategoryNetwork

    @ProductNet
    @Singleton
    @Binds
    abstract fun bindProductNetwork(productNetwork: ProductNetworkRetrofit): ProductNetwork
}