package com.bruno13palhano.core.network.access.impl

import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.Service
import com.bruno13palhano.core.network.access.UserNetwork
import com.bruno13palhano.core.network.model.asNetwork
import okhttp3.ResponseBody
import javax.inject.Inject

internal class UserNetworkRetrofit @Inject constructor(
    private val apiService: Service
) : UserNetwork{
    override suspend fun create(user: User) = apiService.createUser(user = user.asNetwork())

    override suspend fun update(user: User) = apiService.updateUser(user = user.asNetwork())

    override suspend fun login(user: User): ResponseBody = apiService.login(user = user.asNetwork())
}