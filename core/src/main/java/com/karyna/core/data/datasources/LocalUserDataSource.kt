package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.User

interface LocalUserDataSource {
    fun getUser(userId: String): Result<User>
    fun addUser(user: User): Result<Unit>
}