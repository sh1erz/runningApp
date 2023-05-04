package com.karyna.framework.local.datasources

import android.database.sqlite.SQLiteException
import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.mappers.runInputToRun
import com.karyna.framework.mappers.runToDomain
import timber.log.Timber
import javax.inject.Inject

class LocalRunDataSourceImpl @Inject constructor(private val runDao: RunDao) :
    LocalRunDataSource {

    override suspend fun getRuns(userId: String): Result<List<Run>> = try {
        val runs = runDao.getRuns(userId)
        Result.success(runs.map { runToDomain(it) })
    } catch (ex: SQLiteException) {
        Result.failure(ex)
    }

    override suspend fun saveRun(
        id: String,
        runInput: RunInput
    ): Result<Unit> = try {
        runDao.insertRun(runInputToRun(id, runInput))
        Result.success(Unit)
    } catch (ex: SQLiteException) {
        Timber.e(ex)
        Result.failure(ex)
    }

    override suspend fun deleteRun(runId: String): Result<Unit> = try {
        runDao.deleteRun(runId)
        Result.success(Unit)
    } catch (ex: SQLiteException) {
        Timber.e(ex)
        Result.failure(ex)
    }
}