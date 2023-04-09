package com.karyna.core.data

import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.data.datasources.LocalUserDataSource
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunningRepositoryImpl @Inject constructor(
    private val localRunDataSource: LocalRunDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val remoteUserDataSource: RemoteUserDataSource,
) : RunningRepository {
    override suspend fun addUser(user: User): Result<Unit> = withContext(Dispatchers.IO) {
        remoteUserDataSource.addUser(user)
        localUserDataSource.addUser(user)
    }

    override suspend fun getRun(userEmail: String): Result<Run> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getRunsShort(userEmail: String): Result<List<Run>> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userEmail: String): Result<User> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getTopRuns(amount: Int, lastDays: Int?, country: String?): Result<List<RunShort>> =
        withContext(Dispatchers.IO) {
            TODO("Not yet implemented")
        }

    override suspend fun saveRun(
        userId: String,
        date: String,
        location: LocationShort,
        coordinates: List<LatLng>,
        durationS: Long,
        distanceMeters: Int,
        paceMetersInS: Int,
        calories: Int?
    ): Result<Unit> = withContext(Dispatchers.IO) {
        //todo: back: move to input class
        localRunDataSource.saveRun(
            userId = userId,
            date = date,
            location = location,
            coordinates = coordinates,
            durationS = durationS,
            distanceMeters = distanceMeters,
            paceMetersInS = paceMetersInS,
            calories = calories
        )
    }
}