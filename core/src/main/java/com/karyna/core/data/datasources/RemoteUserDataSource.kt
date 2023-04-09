package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.User

interface RemoteUserDataSource {
    fun getUser(userEmail: String): Result<User>
    fun addUser(user: User)
}