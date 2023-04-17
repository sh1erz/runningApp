package com.karyna.framework.local.datasources

import android.database.sqlite.SQLiteException
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
import com.karyna.framework.local.EntryDoesNotExists
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.mappers.runInputToRun
import com.karyna.framework.mappers.runToDomain
import timber.log.Timber
import javax.inject.Inject

class LocalRunDataSourceImpl @Inject constructor(private val runDao: RunDao) :
    LocalRunDataSource {
    override suspend fun getRun(id: Long): Result<Run?> = try {
        val run = runDao.getRun(id)
        if (run != null) {
            Result.Success(runToDomain(run))
        } else {
            Result.Failure(EntryDoesNotExists())
        }
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override suspend fun getRuns(userId: String): Result<List<Run>> = try {
        val runs = runDao.getRuns(userId)
        Result.Success(runs.map { runToDomain(it) })
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
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