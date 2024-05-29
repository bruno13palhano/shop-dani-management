package com.bruno13palhano.core.di

import android.content.Context
import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.di.DatabaseModule
import com.bruno13palhano.core.database.MockDatabaseFactory
import com.bruno13palhano.core.database.MockDriverFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class],
)
object MockDatabaseModule {
    @Provides
    @Singleton
    fun provideShopDatabaseFactoryDriver(
        @ApplicationContext context: Context,
    ): ShopDatabase {
        return MockDatabaseFactory(
            driverFactory = MockDriverFactory(context = context),
        ).createDriver()
    }
}
