package com.karyna.framework.local

import android.database.sqlite.SQLiteException
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.RunDataSource
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.local.dao.UserDao
import com.karyna.framework.mappers.runToDomain
import com.karyna.framework.mappers.runToDto
import javax.inject.Inject

class LocalRunDataSource @Inject constructor(private val runDao: RunDao, private val userDao: UserDao) : RunDataSource {
    override fun getRun(id: Long): Result<Run?> = try {
        val run = runDao.getRun(id)
        val user = run?.userId?.let { userDao.getUser(it) }
        if (run != null && user != null) {
            Result.Success(runToDomain(run, user))
        } else {
            Result.Failure(EntryDoesNotExists())
        }
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override fun getRunsShort(userEmail: String): Result<List<Run>> = try {
        val user = userDao.getUser(userEmail)
        val runs = user?.id?.let { runDao.getRunsShort(it) }
        if (runs != null) {
            Result.Success(runs.map { runToDomain(it, user) })
        } else {
            Result.Failure(EntryDoesNotExists())
        }
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override fun getTopRuns(itemsAmount: Int, lastDays: Int?, country: String?): Result<List<RunShort>> {
        TODO("Not yet implemented")
    }

    override fun saveRun(run: Run): Result<Unit> = try {
        val userId = userDao.getUser(run.user.email)
            ?: throw EntryDoesNotExists("No user with email ${run.user.email}")

        runDao.insertRun(runToDto(run, userId))
        Result.Success(Unit)
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override fun deleteRun(runId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}