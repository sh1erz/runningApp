package com.karyna.framework.local.datasources

import android.database.sqlite.SQLiteException
import com.karyna.core.data.datasources.LocalUserDataSource
import com.karyna.core.domain.User
import com.karyna.framework.local.EntryDoesNotExists
import com.karyna.framework.local.dao.UserDao
import com.karyna.framework.mappers.domainUserToLocal
import com.karyna.framework.mappers.localUserToDomain
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(private val userDao: UserDao) :
    LocalUserDataSource {

    override fun getUser(userId: String): Result<User> = try {
        val user = userDao.getUser(userId)
        user?.let { Result.success(localUserToDomain(user)) } ?: Result.failure(EntryDoesNotExists())
    } catch (ex: SQLiteException) {
        Result.failure(ex)
    }

    override fun addUser(user: User): Result<Unit> = try {
        userDao.insertUser(domainUserToLocal(user))
        Result.success(Unit)
    } catch (ex: SQLiteException) {
        Result.failure(ex)
    }

    override fun setWeight(userId: String, weight: Float?): Result<Unit> = try {
        userDao.updateWeight(userId, weight)
        Result.success(Unit)
    } catch (ex: SQLiteException) {
        Result.failure(ex)
    }
}