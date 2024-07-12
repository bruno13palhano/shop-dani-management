package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.model.UserCodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {
    override suspend fun login(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (user.uid.isEmpty()) {
            onError(UserCodeResponse.LOGIN_SERVER_ERROR)
        } else {
            onSuccess()
        }
    }

    override suspend fun create(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: (uid: String) -> Unit
    ) {
        if (user.uid.isEmpty()) {
            onSuccess("")
        } else {
            onError(UserCodeResponse.INSERT_SERVER_ERROR)
        }
    }

    override suspend fun update(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (user.uid.isEmpty()) {
            onError(UserCodeResponse.UPDATE_SERVER_ERROR)
        } else {
            onSuccess()
        }
    }

    override fun getById(
        uid: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ): Flow<User> {
        return flow {
            while (true) emit(
                User(
                    uid = "ABCD1234",
                    username = "User",
                    email = "user@email.com",
                    password = "",
                    photo = "",
                    timestamp = "2024-01-01T20:00:00Z"
                )
            )
        }
    }

    override fun getCurrentUser(
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ): Flow<User> {
        return flow {
            while (true) emit(
                User(
                    uid = "ABCD1234",
                    username = "User",
                    email = "user@email.com",
                    password = "",
                    photo = "",
                    timestamp = "2024-01-01T20:00:00Z"
                )
            )
        }
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun authenticated(): Flow<Boolean> {
        return flow { while (true) emit(true) }
    }

    override suspend fun logout(
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
    }

    override suspend fun updateUserPassword(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
    }
}