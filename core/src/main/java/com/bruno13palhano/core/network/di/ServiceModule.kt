package com.bruno13palhano.core.network.di

import android.content.Context
import com.bruno13palhano.core.BuildConfig
import com.bruno13palhano.core.network.AuthenticatorInterceptor
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.SessionManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DefaultSessionManager

@Qualifier
annotation class DefaultAuthenticationInterceptor

private const val BASE_URL = BuildConfig.apiUrl

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun provideApiService(
        @DefaultAuthenticationInterceptor authenticatorInterceptor: Interceptor,
    ): Service {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client =
            OkHttpClient.Builder()
                .addInterceptor(authenticatorInterceptor)
                .addInterceptor(interceptor)
                .build()
        val retrofit =
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .client(client)
                .build()

        val apiService: Service by lazy { retrofit.create(Service::class.java) }

        return apiService
    }

    @Singleton
    @DefaultSessionManager
    @Provides
    fun provideSession(
        @ApplicationContext context: Context,
    ): SessionManager = SessionManager(context)

    @Singleton
    @DefaultAuthenticationInterceptor
    @Provides
    fun provideAuthenticationInterceptor(
        @DefaultSessionManager sessionManager: SessionManager,
    ): Interceptor = AuthenticatorInterceptor(sessionManager)
}
