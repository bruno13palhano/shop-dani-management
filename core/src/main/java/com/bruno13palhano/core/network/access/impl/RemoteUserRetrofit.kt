package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.data.di.IOScope
import com.bruno13palhano.core.model.UserCodeResponse
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.RemoteUserData
import com.bruno13palhano.core.network.model.UserNet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CODE_RESPONSE = "CODE"
private const val AUTHORIZATION = "Authorization"

internal class RemoteUserRetrofit
    @Inject
    constructor(
        private val apiService: Service,
        @IOScope private val ioScope: CoroutineScope
    ) : RemoteUserData {
        override suspend fun create(
            user: UserNet,
            onError: (error: Int) -> Unit,
            onSuccess: (userNet: UserNet) -> Unit
        ) {
            ioScope.launch {
                try {
                    val response = apiService.createUser(user = user)
                    response.headers()[CODE_RESPONSE]?.let { code ->
                        val responseCode = codeToInt(code)
                        if (responseCode == UserCodeResponse.OK) {
                            response.body()?.let { newUserNet ->
                                onSuccess(newUserNet)
                            }
                        } else {
                            onError(responseCode)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError(UserCodeResponse.INSERT_SERVER_ERROR)
                }
            }
        }

        override suspend fun update(
            user: UserNet,
            onError: (Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            ioScope.launch {
                try {
                    val response = apiService.updateUser(user = user)
                    response.headers()[CODE_RESPONSE]?.let { code ->
                        val responseCode = codeToInt(code)
                        if (responseCode == UserCodeResponse.OK) {
                            onSuccess()
                        } else {
                            onError(responseCode)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError(UserCodeResponse.UPDATE_SERVER_ERROR)
                }
            }
        }

        override suspend fun login(
            user: UserNet,
            onError: (Int) -> Unit,
            onSuccess: (token: String) -> Unit
        ) {
            ioScope.launch {
                try {
                    val response = apiService.login(user = user)
                    response.headers()[CODE_RESPONSE]?.let { code ->
                        val responseCode = codeToInt(code)
                        if (responseCode == UserCodeResponse.OK) {
                            response.headers()[AUTHORIZATION]?.let { token ->
                                onSuccess(token)
                            }
                        } else {
                            onError(responseCode)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError(UserCodeResponse.LOGIN_SERVER_ERROR)
                }
            }
        }

        override suspend fun authenticated(token: String) = apiService.authenticated(token = token)

        override suspend fun getByUsername(username: String): UserNet = apiService.getUser(username = username)

        override suspend fun updateUserPassword(
            user: UserNet,
            onError: (Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                val response = apiService.updateUserPassword(user = user)
                response.headers()[CODE_RESPONSE]?.let { code ->
                    val responseCode = codeToInt(code)
                    if (responseCode == UserCodeResponse.OK) {
                        onSuccess()
                    } else {
                        onError(responseCode)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(UserCodeResponse.UPDATE_SERVER_ERROR)
            }
        }

        private fun codeToInt(code: String) =
            try {
                code.toInt()
            } catch (e: Exception) {
                0
            }
    }