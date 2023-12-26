package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.User
import okhttp3.ResponseBody

interface UserNetwork {
    suspend fun create(user: User)
    suspend fun update(user: User)
    suspend fun login(user: User): ResponseBody
    suspend fun authenticated(token: String): Boolean
}