package com.bruno13palhano.core.data.repository.user

import com.bruno13palhano.core.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit)
    suspend fun create(user: User, onError: (error: Int) -> Unit, onSuccess: (id: Long) -> Unit)
    suspend fun update(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit)
    fun getById(userId: Long, onError: (error: Int) -> Unit, onSuccess: () -> Unit): Flow<User>
    fun getCurrentUser(onError: (error: Int) -> Unit, onSuccess: () -> Unit): Flow<User>
    fun isAuthenticated(): Boolean
    fun authenticated(): Flow<Boolean>
    fun logout()
    suspend fun updateUserPassword(user: User, onError: (error: Int) -> Unit, onSuccess: () -> Unit)
}