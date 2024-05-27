package com.bruno13palhano.core.data.repository.user

import android.content.Context
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalUserLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.data.repository.userNetToUser
import com.bruno13palhano.core.data.repository.userToUserNet
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.model.UserCodeResponse
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.di.DefaultUserNet
import com.bruno13palhano.core.network.SessionManager
import com.bruno13palhano.core.network.di.DefaultSessionManager
import com.bruno13palhano.core.network.model.UserNet
import com.bruno13palhano.core.sync.Sync
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

private const val CODE_RESPONSE = "CODE"
private const val AUTHORIZATION = "Authorization"

internal class DefaultUserRepository @Inject constructor(
    @DefaultUserNet private val userNetwork: UserNetwork,
    @InternalUserLight private val userData: UserData,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    @DefaultSessionManager private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : UserRepository {
    override suspend fun login(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val response = userNetwork.login(user = userToUserNet(user))
                response.headers()[CODE_RESPONSE]?.let { code ->
                    val responseCode = codeToInt(code = code)
                    if (responseCode == UserCodeResponse.OK) {
                        saveToken(response = response)
                        saveUser(
                            username = user.username,
                            onError = onError, onSuccess = { onSuccess() }
                        )
                        Sync.initialize(context)
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

    override suspend fun create(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val response = userNetwork.create(user = userToUserNet(user))
                response.headers()[CODE_RESPONSE]?.let { code ->
                    val responseCode = codeToInt(code = code)
                    if (responseCode == UserCodeResponse.OK) {
                        createUser(response = response, onError = onError, onSuccess = onSuccess)
                    } else {
                        onError(responseCode)
                    }
                }
            } catch (e: Exception) {
                onError(UserCodeResponse.INSERT_SERVER_ERROR)
            }
        }
    }

    override suspend fun update(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val response = userNetwork.update(user = userToUserNet(user)).headers()[CODE_RESPONSE]
                response?.let { code ->
                    val responseCode = codeToInt(code = code)
                    if (responseCode == UserCodeResponse.OK) {
                        userData.update(user = user, onError = onError, onSuccess = onSuccess)
                    } else {
                        onError(responseCode)
                    }
                }
            } catch (e: Exception) {
                onError(UserCodeResponse.UPDATE_SERVER_ERROR)
            }
        }
    }

    override fun getById(
        userId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ): Flow<User> {
        return userData.getById(userId, onError = onError, onSuccess = onSuccess)
    }

    override fun getCurrentUser(onError: (error: Int) -> Unit, onSuccess: () -> Unit): Flow<User> {
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
        onSuccess: () -> Unit
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val response = userNetwork.updateUserPassword(userToUserNet(user))
                response.headers()[CODE_RESPONSE]?.let { code ->
                    val responseCode = codeToInt(code = code)
                    if (responseCode == UserCodeResponse.OK) {
                        userData.updateUserPassword(
                            user = user,
                            onError = onError,
                            onSuccess = onSuccess
                        )
                    } else {
                        onError(responseCode)
                    }
                }
            } catch (e: Exception) {
                onError(UserCodeResponse.UPDATE_SERVER_ERROR)
            }
        }
    }

    private fun saveToken(response: Response<Unit>) {
        val token = response.headers()[AUTHORIZATION]

        if (token != null) {
            sessionManager.saveAuthToken(token = token)
        }
    }

    private suspend fun createUser(
        response: Response<UserNet>,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ) {
        val newUser = response.body()?.let { userNetToUser(it) }
        if (newUser != null) {
            userData.insert(user = newUser, onError = onError, onSuccess = onSuccess)
        }
    }

    private suspend fun saveUser(
        username: String,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ) {
        val newUser = userNetwork.getByUsername(username = username)
        sessionManager.saveCurrentUserId(id = newUser.id)
        userData.insert(
            user = userNetToUser(newUser),
            onError = onError,
            onSuccess = onSuccess
        )
    }

    private fun codeToInt(code: String) = try { code.toInt() } catch (e: Exception) { 0 }
}