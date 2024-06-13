package com.bruno13palhano.core.data.repository.user

import com.bruno13palhano.core.data.di.DefaultSessionManager
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalUser
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.userNetToUser
import com.bruno13palhano.core.data.repository.userToUserNet
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.SessionManager
import com.bruno13palhano.core.network.access.RemoteUserData
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
                CoroutineScope(ioDispatcher).launch {
                    saveUser(
                        username = user.username,
                        password = user.password,
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
            val uid = getCurrentUserUid() ?: ""

            return userData.getById(uid = uid, onError = onError, onSuccess = onSuccess)
        }

        override fun isAuthenticated(): Boolean {
            val uid = getCurrentUserUid()

            uid?.let {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        val authenticated = remoteUserData.authenticated(it)
                        if (!authenticated) {
                            saveUserCredentials(uid = "", password = "")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            return !uid.isNullOrEmpty()
        }

        override fun authenticated(): Flow<Boolean> {
            val token = sessionManager.fetchCurrentUserUid()

            token?.let {
                CoroutineScope(ioDispatcher).launch {
                    try {
                        val authenticated = remoteUserData.authenticated(it)
                        if (!authenticated) {
                            saveUserCredentials(uid = "", password = "")
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

        override suspend fun logout(
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            saveUserCredentials(uid = "", password = "")
            remoteUserData.logout(
                onError = onError,
                onSuccess = onSuccess
            )
        }

        override suspend fun updateUserPassword(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            val oldPassword = getCurrentUserPassword() ?: ""

            remoteUserData.updateUserPassword(
                oldPassword = user.password,
                newPassword = oldPassword,
                email = user.email,
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

        private suspend fun createUser(
            newUser: UserNet,
            onError: (error: Int) -> Unit,
            onSuccess: (uid: String) -> Unit
        ) {
            userData.insert(user = userNetToUser(newUser), onError = onError, onSuccess = onSuccess)
        }

        private suspend fun saveUser(
            username: String,
            password: String,
            onError: (error: Int) -> Unit,
            onSuccess: (uid: String) -> Unit
        ) {
            val newUser = remoteUserData.getByUsername(username = username)
            saveUserCredentials(uid = newUser.uid, password)
            userData.insert(
                user = userNetToUser(newUser),
                onError = onError,
                onSuccess = onSuccess
            )
        }

        private fun saveUserCredentials(
            uid: String,
            password: String
        ) {
            sessionManager.saveCurrentUserUid(uid = uid)
            sessionManager.saveCurrentUserPassword(password = password)
        }

        private fun getCurrentUserUid(): String? {
            return sessionManager.fetchCurrentUserUid()
        }

        private fun getCurrentUserPassword(): String? {
            return sessionManager.fetchCurrentUserPassword()
        }
    }