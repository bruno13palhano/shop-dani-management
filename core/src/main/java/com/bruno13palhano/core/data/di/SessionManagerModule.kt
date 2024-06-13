package com.bruno13palhano.core.data.di

import android.content.Context
import com.bruno13palhano.core.network.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DefaultSessionManager

@Module
@InstallIn(SingletonComponent::class)
internal object SessionManagerModule {
    @Singleton
    @DefaultSessionManager
    @Provides
    fun provideSession(
        @ApplicationContext context: Context
    ): SessionManager = SessionManager(context)
}