package com.bruno13palhano.core.data.repository.user

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalUser
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.userNetToUser
import com.bruno13palhano.core.data.repository.userToUserNet
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.SessionManager
import com.bruno13palhano.core.network.access.RemoteUserData
import com.bruno13palhano.core.network.di.DefaultSessionManager
import com.bruno13palhano.core.network.di.FirebaseUser
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
        @FirebaseUser private val remoteUserData: RemoteUserData,
        @InternalUser private val userData: UserData,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
        @DefaultSessionManager private val sessionManager: SessionManager
    ) : UserRepository {
        override suspend fun login(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            remoteUserData.login(
                user = userToUserNet(user),
                onError = onError
            ) {
                saveToken(token = it)
                CoroutineScope(ioDispatcher).launch {
                    saveUser(
                        username = user.username,
                        onError = onError,
                        onSuccess = { onSuccess() }
                    )
                }
            }
        }

        override suspend fun create(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: (uid: String) -> Unit
        ) {
            remoteUserData.create(
                user = userToUserNet(user),
                onError = onError
            ) { userNet ->
                CoroutineScope(ioDispatcher).launch {
                    createUser(
                        newUser = userNet,
                        onError = onError,
                        onSuccess = onSuccess
                    )
                }
            }
        }

        override suspend fun update(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            remoteUserData.update(
                user = userToUserNet(user),
                onError = onError
            ) {
                CoroutineScope(ioDispatcher).launch {
                    userData.update(
                        user = user,
                        onError = onError,
                        onSuccess = onSuccess
                    )
                }
            }
        }

        override fun getById(
            uid: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ): Flow<User> {
            return userData.getById(uid = uid, onError = onError, onSuccess = onSuccess)
        }

        override fun getCurrentUser(
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ): Flow<User> {
            val uid = sessionManager.fetchCurrentUserUid() ?: ""

            return userData.getById(uid = uid, onError = onError, onSuccess = onSuccess)
        }

        override fun isAuthenticated(): Boolean {
            val token = sessionManager.fetchAuthToken()

            token?.let {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        val authenticated = remoteUserData.authenticated(it)
                        if (!authenticated) {
                            saveToken(token = "")
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
                        val authenticated = remoteUserData.authenticated(it)
                        if (!authenticated) {
                            saveToken(token = "")
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
            saveToken(token = "")
            sessionManager.saveCurrentUserUid(uid = "")
        }

        override suspend fun updateUserPassword(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            remoteUserData.updateUserPassword(
                user = userToUserNet(user),
                onError = onError
            ) {
                CoroutineScope(ioDispatcher).launch {
                    userData.updateUserPassword(
                        user = user,
                        onError = onError,
                        onSuccess = onSuccess
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
            onSuccess: (uid: String) -> Unit
        ) {
            userData.insert(user = userNetToUser(newUser), onError = onError, onSuccess = onSuccess)
        }

        private suspend fun saveUser(
            username: String,
            onError: (error: Int) -> Unit,
            onSuccess: (uid: String) -> Unit
        ) {
            val newUser = remoteUserData.getByUsername(username = username)
            sessionManager.saveCurrentUserUid(uid = newUser.uid)
            userData.insert(
                user = userNetToUser(newUser),
                onError = onError,
                onSuccess = onSuccess
            )
        }
    }