package com.bruno13palhano.core.data.repository.user

import com.bruno13palhano.core.model.User
import kotlinx.coroutines.flow.Flow

interface UserData {
    suspend fun insert(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit,
    )

    suspend fun update(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )

    fun getById(
        userId: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    ): Flow<User>

    suspend fun updateUserPassword(
        user: User,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )
}
