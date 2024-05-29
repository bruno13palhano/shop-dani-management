package com.bruno13palhano.core.data.repository.user

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalUserLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.userNetToUser
import com.bruno13palhano.core.data.repository.userToUserNet
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.SessionManager
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.di.DefaultSessionManager
import com.bruno13palhano.core.network.di.DefaultUserNet
import com.bruno13palhano.core.network.model.UserNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultUserRepository
    @Inject
    constructor(
        @DefaultUserNet private val userNetwork: UserNetwork,
        @InternalUserLight private val userData: UserData,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
        @DefaultSessionManager private val sessionManager: SessionManager,
    ) : UserRepository {
        override suspend fun login(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            userNetwork.login(
                user = userToUserNet(user),
                onError = onError,
            ) {
                saveToken(token = it)
                CoroutineScope(ioDispatcher).launch {
                    saveUser(
                        username = user.username,
                        onError = onError,
                        onSuccess = { onSuccess() },
                    )
                }
            }
        }

        override suspend fun create(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ) {
            userNetwork.create(
                user = userToUserNet(user),
                onError = onError,
            ) { userNet ->
                CoroutineScope(ioDispatcher).launch {
                    createUser(
                        newUser = userNet,
                        onError = onError,
                        onSuccess = onSuccess,
                    )
                }
            }
        }

        override suspend fun update(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            userNetwork.update(
                user = userToUserNet(user),
                onError = onError,
            ) {
                CoroutineScope(ioDispatcher).launch {
                    userData.update(
                        user = user,
                        onError = onError,
                        onSuccess = onSuccess,
                    )
                }
            }
        }

        override fun getById(
            userId: Long,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ): Flow<User> {
            return userData.getById(userId, onError = onError, onSuccess = onSuccess)
        }

        override fun getCurrentUser(
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ): Flow<User> {
            val userId = sessionManager.fetchCurrentUserId()

            return userData.getById(userId = userId, onError = onError, onSuccess = onSuccess)
        }

        override fun isAuthenticated(): Boolean {
            val token = sessionManager.fetchAuthToken()

            token?.let {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        val authenticated = userNetwork.authenticated(it)
                        if (!authenticated) {
                            sessionManager.saveAuthToken(token = "")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            return !token.isNullOrEmpty()
        }

        override fun authenticated(): Flow<Boolean> {
            val token = sessionManager.fetchAuthToken()

            token?.let {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        val authenticated = userNetwork.authenticated(it)
                        if (!authenticated) {
                            sessionManager.saveAuthToken(token = "")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            return flow {
                while (true) {
                    emit(!token.isNullOrEmpty())
                    delay(1000)
                }
            }
        }

        override fun logout() {
            sessionManager.saveAuthToken(token = "")
            sessionManager.saveCurrentUserId(id = 0L)
        }

        override suspend fun updateUserPassword(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit,
        ) {
            userNetwork.updateUserPassword(
                user = userToUserNet(user),
                onError = onError,
            ) {
                CoroutineScope(ioDispatcher).launch {
                    userData.updateUserPassword(
                        user = user,
                        onError = onError,
                        onSuccess = onSuccess,
                    )
                }
            }
        }

        private fun saveToken(token: String) {
            sessionManager.saveAuthToken(token = token)
        }

        private suspend fun createUser(
            newUser: UserNet,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ) {
            userData.insert(user = userNetToUser(newUser), onError = onError, onSuccess = onSuccess)
        }

        private suspend fun saveUser(
            username: String,
            onError: (error: Int) -> Unit,
            onSuccess: (id: Long) -> Unit,
        ) {
            val newUser = userNetwork.getByUsername(username = username)
            sessionManager.saveCurrentUserId(id = newUser.id)
            userData.insert(
                user = userNetToUser(newUser),
                onError = onError,
                onSuccess = onSuccess,
            )
        }
    }
