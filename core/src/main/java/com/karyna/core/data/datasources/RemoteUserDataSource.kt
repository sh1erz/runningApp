package com.karyna.core.data.datasources

import com.karyna.core.domain.User

interface RemoteUserDataSource {
    suspend fun getUser(userId: String): Result<User>
    suspend fun addUser(user: User)
    suspend fun setWeight(userId: String, weight: Float?): Result<Unit>
}