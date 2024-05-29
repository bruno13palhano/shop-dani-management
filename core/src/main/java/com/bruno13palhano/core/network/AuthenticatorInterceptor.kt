package com.bruno13palhano.core.network

import com.bruno13palhano.core.network.di.DefaultSessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorInterceptor
    @Inject
    constructor(
        @DefaultSessionManager private val sessionManager: SessionManager,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()

            sessionManager.fetchAuthToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            return chain.proceed(requestBuilder.build())
        }
    }
