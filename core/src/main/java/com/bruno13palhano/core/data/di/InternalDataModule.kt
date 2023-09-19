package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.data.repository.category.CategoryRoom
import com.bruno13palhano.core.data.repository.category.InternalCategoryData
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
}