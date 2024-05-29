package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.network.model.UserNet
import retrofit2.Response

interface UserNetwork {
    suspend fun create(user: UserNet): Response<UserNet>

    suspend fun update(user: UserNet): Response<Int>

    suspend fun login(user: UserNet): Response<Unit>

    suspend fun authenticated(token: String): Boolean

    suspend fun getByUsername(username: String): UserNet

    suspend fun updateUserPassword(user: UserNet): Response<Int>
}
