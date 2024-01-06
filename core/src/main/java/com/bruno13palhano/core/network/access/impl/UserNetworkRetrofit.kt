package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.model.asExternal
import com.bruno13palhano.core.network.model.asNetwork
import retrofit2.Response
import javax.inject.Inject

internal class UserNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : UserNetwork{
    override suspend fun create(user: User) =
        apiService.createUser(user = user.asNetwork())

    override suspend fun update(user: User) = apiService.updateUser(user = user.asNetwork())

    override suspend fun login(user: User): Response<Unit> = apiService.login(user = user.asNetwork())

    override suspend fun authenticated(token: String) = apiService.authenticated(token = token)

    override suspend fun getByUsername(username: String): User =
        apiService.getUser(username = username).asExternal()

    override suspend fun updateUserPassword(user: User) =
        apiService.updateUserPassword(user = user.asNetwork())
}