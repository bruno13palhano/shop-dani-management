package com.bruno13palhano.core.data.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.core.data.database.AppDatabase
import com.bruno13palhano.core.data.database.CategoryConverter
import com.bruno13palhano.core.data.database.dao.CategoryDao
import com.bruno13palhano.core.data.database.dao.ProductDao
import com.bruno13palhano.core.data.database.dao.SearchCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {

    @Provides
    fun providesProductDao(database: AppDatabase): ProductDao {
        return database.productDao
    }

    @Provides
    fun providesCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao
    }

    @Provides
    fun providesSearchCacheDao(database: AppDatabase): SearchCacheDao {
        return database.searchCacheDao
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "shop_dani_management_database"
        )
            .addTypeConverter(CategoryConverter())
            .fallbackToDestructiveMigration()
            .build()
    }
}