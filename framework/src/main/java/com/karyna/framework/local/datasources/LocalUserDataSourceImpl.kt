package com.karyna.framework.local.datasources

import android.database.sqlite.SQLiteException
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.LocalUserDataSource
import com.karyna.core.domain.User
import com.karyna.framework.local.EntryDoesNotExists
import com.karyna.framework.local.dao.UserDao
import com.karyna.framework.mappers.userToDomain
import com.karyna.framework.mappers.userToDto
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(private val userDao: UserDao) :
    LocalUserDataSource {

    override fun getUser(userId: String): Result<User> = try {
        val user = userDao.getUser(userId)
        user?.let { Result.Success(userToDomain(user)) } ?: Result.Failure(EntryDoesNotExists())
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override fun addUser(user: User): Result<Unit> = try {
        userDao.insertUser(userToDto(user))
        Result.Success(Unit)
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }
}