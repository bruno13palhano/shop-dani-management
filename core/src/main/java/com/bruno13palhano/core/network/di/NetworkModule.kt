package com.bruno13palhano.core.network.di

import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.network.CrudNetwork
import com.bruno13palhano.core.network.access.CategoryNetworkRetrofit
import com.bruno13palhano.core.network.access.ProductNetworkRetrofit
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class DefaultCategoryNet

@Qualifier
internal annotation class DefaultProductNet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @DefaultCategoryNet
    @Singleton
    @Binds
    abstract fun bindCategoryNetwork(
        categoryNetwork: CategoryNetworkRetrofit
    ): CrudNetwork<Category>

    @DefaultProductNet
    @Singleton
    @Binds
    abstract fun bindProductNetwork(
        productNetwork: ProductNetworkRetrofit
    ): CrudNetwork<Product>
}