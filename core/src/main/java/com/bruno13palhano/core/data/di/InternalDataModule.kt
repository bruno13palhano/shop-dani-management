package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.data.repository.category.CategoryRoom
import com.bruno13palhano.core.data.repository.category.InternalCategoryData
import com.bruno13palhano.core.data.repository.customer.CustomerLight
import com.bruno13palhano.core.data.repository.customer.CustomerRoom
import com.bruno13palhano.core.data.repository.customer.InternalCustomerData
import com.bruno13palhano.core.model.Customer
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

@InstallIn(SingletonComponent::class)
@Module
internal abstract class InternalDataModule {

    @InternalCategoryRoom
    @Singleton
    @Binds
    abstract fun bindInternalCategoryRoom(repository: CategoryRoom): InternalCategoryData

    @InternalCategoryLight
    @Singleton
    @Binds
    abstract fun bindInternalCategoryLight(repository: CategoryLight): InternalCategoryData

    @InternalCustomerRoom
    @Singleton
    @Binds
    abstract fun bindInternalCustomerRoom(repository: CustomerRoom): InternalCustomerData

    @InternalCustomerLight
    @Singleton
    @Binds
    abstract fun bindInternalCustomerLight(repository: CustomerLight): InternalCustomerData
}