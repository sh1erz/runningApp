package com.karyna.core.data.datasources

import com.karyna.core.domain.User

interface LocalUserDataSource {
    fun getUser(userId: String): Result<User>
    fun addUser(user: User): Result<Unit>
    fun setWeight(userId: String, weight: Float?): Result<Unit>
}