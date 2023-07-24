package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.repository.ProductRepository
import com.bruno13palhano.core.model.Product
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Annotation to inject [ProductRepository].
 *
 * Injects the default [ProductRepository] implementation.
 */
@Qualifier
annotation class DefaultProductRepository

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @DefaultProductRepository
    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: ProductRepository): DataOperations<Product>
}