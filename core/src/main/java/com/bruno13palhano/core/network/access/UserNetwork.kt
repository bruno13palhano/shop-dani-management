package com.bruno13palhano.core.network.access

import com.bruno13palhano.core.model.User

interface UserNetwork {
    suspend fun create(user: User)
    suspend fun update(user: User)
    suspend fun getById(userId: Long): User
}