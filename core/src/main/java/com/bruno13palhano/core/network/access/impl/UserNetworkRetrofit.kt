package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.model.UserNet
import retrofit2.Response
import javax.inject.Inject

internal class UserNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : UserNetwork{
    override suspend fun create(user: UserNet) = apiService.createUser(user = user)

    override suspend fun update(user: UserNet) = apiService.updateUser(user = user)

    override suspend fun login(user: UserNet): Response<Unit> = apiService.login(user = user)

    override suspend fun authenticated(token: String) = apiService.authenticated(token = token)

    override suspend fun getByUsername(username: String): UserNet =
        apiService.getUser(username = username)

    override suspend fun updateUserPassword(user: UserNet) =
        apiService.updateUserPassword(user = user)
}