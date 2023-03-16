package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.User

interface UserDataSource {
    fun getUser(userEmail: String): Result<User>
//    fun saveUser(user: User): Result<Unit>
}