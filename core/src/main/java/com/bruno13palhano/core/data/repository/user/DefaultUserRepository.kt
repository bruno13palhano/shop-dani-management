package com.bruno13palhano.core.data.repository.user

import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.InternalUserLight
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.di.DefaultUserNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    @DefaultUserNet private val userNetwork: UserNetwork,
    @InternalUserLight private val userData: UserData,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun login(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            try {
                userNetwork.login(user = user)
            } catch (e: Exception) {
                onError(Errors.LOGIN_SERVER_ERROR)
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
                userNetwork.create(user = user)
                userData.insert(user = user, onError = onError, onSuccess = onSuccess)
            } catch (e: Exception) {
                onError(Errors.INSERT_SERVER_ERROR)
            }
        }
    }

    override suspend fun update(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            try {
                userNetwork.update(user = user)
                userData.update(user = user, onError = onError, onSuccess = onSuccess)
            } catch (e: Exception) {
                onError(Errors.UPDATE_SERVER_ERROR)
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
}