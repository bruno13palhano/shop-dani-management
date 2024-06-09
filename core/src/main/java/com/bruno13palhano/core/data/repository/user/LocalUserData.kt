package com.bruno13palhano.core.data.repository.user

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import cache.UsersTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class LocalUserData
    @Inject
    constructor(
        private val usersTableQueries: UsersTableQueries,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : UserData {
        override suspend fun insert(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: (uid: String) -> Unit
        ) {
            try {
                usersTableQueries.insertWithId(
                    uid = user.uid,
                    username = user.username,
                    password = user.password,
                    email = user.email,
                    photo = user.photo,
                    role = user.role,
                    enabled = user.enabled,
                    timestamp = user.timestamp
                )
                onSuccess(user.uid)
            } catch (e: Exception) {
                e.printStackTrace()
                onError(Errors.INSERT_DATABASE_ERROR)
            }
        }

        override suspend fun update(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                usersTableQueries.update(
                    username = user.username,
                    photo = user.photo,
                    timestamp = user.timestamp,
                    uid = user.uid
                )
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(Errors.UPDATE_DATABASE_ERROR)
            }
        }

        override fun getById(
            uid: String,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ): Flow<User> {
            return usersTableQueries.getById(uid = uid, mapper = ::mapUser)
                .asFlow().mapToOne(ioDispatcher)
                .catch { it.printStackTrace() }
        }

        override suspend fun updateUserPassword(
            user: User,
            onError: (error: Int) -> Unit,
            onSuccess: () -> Unit
        ) {
            try {
                usersTableQueries.updatePassword(timestamp = user.timestamp, uid = user.uid)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(Errors.UPDATE_DATABASE_ERROR)
            }
        }

        private fun mapUser(
            uid: String,
            username: String,
            email: String,
            password: String,
            photo: ByteArray,
            role: String,
            enabled: Boolean,
            timestamp: String
        ) = User(
            uid = uid,
            username = username,
            email = email,
            password = password,
            photo = photo,
            role = role,
            enabled = enabled,
            timestamp = timestamp
        )
    }