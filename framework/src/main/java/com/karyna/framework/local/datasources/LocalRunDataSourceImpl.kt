package com.karyna.framework.local.datasources

import android.database.sqlite.SQLiteException
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
import com.karyna.core.domain.run.RunShort
import com.karyna.framework.local.EntryDoesNotExists
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.local.dao.UserDao
import com.karyna.framework.mappers.runInputToRun
import com.karyna.framework.mappers.runToDomain
import timber.log.Timber
import javax.inject.Inject

class LocalRunDataSourceImpl @Inject constructor(private val runDao: RunDao, private val userDao: UserDao) :
    LocalRunDataSource {
    override suspend fun getRun(id: Long): Result<Run?> = try {
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

    override suspend fun getRuns(userId: String): Result<List<Run>> = try {
        val user = userDao.getUser(userId)
        val runs = user?.id?.let { runDao.getRuns(it) }
        if (runs != null) {
            Result.Success(runs.map { runToDomain(it, user) })
        } else {
            Result.Failure(EntryDoesNotExists())
        }
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override suspend fun getTopRuns(itemsAmount: Int, lastDays: Int?, country: String?): Result<List<RunShort>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRun(
        id: String,
        runInput: RunInput
    ): Result<Unit> = try {
        runDao.insertRun(runInputToRun(id, runInput))
        Result.Success(Unit)
    } catch (ex: SQLiteException) {
        Timber.e(ex)
        Result.Failure(ex)
    }

    override suspend fun deleteRun(runId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}