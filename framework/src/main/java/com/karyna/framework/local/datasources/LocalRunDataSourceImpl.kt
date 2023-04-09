package com.karyna.framework.local.datasources

import android.database.sqlite.SQLiteException
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort
import com.karyna.framework.local.EntryDoesNotExists
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.local.dao.UserDao
import com.karyna.framework.mappers.runInputToDto
import com.karyna.framework.mappers.runToDomain
import javax.inject.Inject

class LocalRunDataSourceImpl @Inject constructor(private val runDao: RunDao, private val userDao: UserDao) :
    LocalRunDataSource {
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

    override fun getRunsShort(userId: String): Result<List<Run>> = try {
        val user = userDao.getUser(userId)
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

    override fun saveRun(
        userId: String,
        date: String,
        location: LocationShort,
        coordinates: List<LatLng>,
        durationS: Long,
        distanceMeters: Int,
        paceMetersInS: Int,
        calories: Int?
    ): Result<Unit> = try {
        runDao.insertRun(
            runInputToDto(
                userId = userId,
                date = date,
                location = location,
                coordinates = coordinates,
                durationS = durationS,
                distanceMeters = distanceMeters,
                paceMetersInS = paceMetersInS,
                calories = calories
            )
        )
        Result.Success(Unit)
    } catch (ex: SQLiteException) {
        Result.Failure(ex)
    }

    override fun deleteRun(runId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}