package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.model.UserCodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestUserRepository : UserRepository {
    override suspend fun login(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (user.id == 0L) {
            onError(UserCodeResponse.LOGIN_SERVER_ERROR)
        } else {
            onSuccess()
        }
    }

    override suspend fun create(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ) {
        if (user.id == 0L) {
            onSuccess(1L)
        } else {
            onError(UserCodeResponse.INSERT_SERVER_ERROR)
        }
    }

    override suspend fun update(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (user.id == 0L) {
            onError(UserCodeResponse.UPDATE_SERVER_ERROR)
        } else {
            onSuccess()
        }
    }

    override fun getById(
        userId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ): Flow<User> {
        return flow {
            while (true) emit(
                User(
                    id = 1L,
                    username = "User",
                    email = "user@email.com",
                    password = "",
                    photo = byteArrayOf(),
                    role = "ROLE_USER",
                    enabled = true,
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
                    id = 1L,
                    username = "User",
                    email = "user@email.com",
                    password = "",
                    photo = byteArrayOf(),
                    role = "ROLE_USER",
                    enabled = true,
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

    override fun logout() {
    }

    override suspend fun updateUserPassword(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
    }
}