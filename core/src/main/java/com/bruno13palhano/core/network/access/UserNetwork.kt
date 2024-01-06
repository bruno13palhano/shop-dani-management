package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.model.UserNet
import retrofit2.Response

interface UserNetwork {
    suspend fun create(user: User): Response<UserNet>
    suspend fun update(user: User): Response<Int>
    suspend fun login(user: User): Response<Unit>
    suspend fun authenticated(token: String): Boolean
    suspend fun getByUsername(username: String): User
    suspend fun updateUserPassword(user: User): Response<Int>
}