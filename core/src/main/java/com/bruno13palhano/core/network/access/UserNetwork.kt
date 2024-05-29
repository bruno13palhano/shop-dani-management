package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.network.model.UserNet

interface UserNetwork {
    suspend fun create(
        user: UserNet,
        onError: (error: Int) -> Unit,
        onSuccess: (userNet: UserNet) -> Unit,
    )

    suspend fun update(
        user: UserNet,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )

    suspend fun login(
        user: UserNet,
        onError: (error: Int) -> Unit,
        onSuccess: (token: String) -> Unit,
    )

    suspend fun authenticated(token: String): Boolean

    suspend fun getByUsername(username: String): UserNet

    suspend fun updateUserPassword(
        user: UserNet,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit,
    )
}
