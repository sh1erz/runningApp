package com.karyna.core.data

import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.data.datasources.LocalUserDataSource
import com.karyna.core.data.datasources.RemoteRunDataSource
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
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
    private val remoteRunDataSource: RemoteRunDataSource,
) : RunningRepository {
    override suspend fun addUser(user: User, addToRemote: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        if (addToRemote) {
            remoteUserDataSource.addUser(user)
        }
        localUserDataSource.addUser(user)
    }

    override suspend fun getRun(userId: String): Result<Run> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getRuns(userId: String): Result<List<Run>> = withContext(Dispatchers.IO) {
        val response = remoteRunDataSource.getRuns(userId)
        if (response is Result.Failure) {
            localRunDataSource.getRuns(userId)
        } else response
    }

    override suspend fun getUser(userId: String): Result<User> = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getTopRuns(amount: Int, lastDays: Int?, country: String?): Result<List<RunShort>> =
        withContext(Dispatchers.IO) {
            TODO("Not yet implemented")
        }

    override suspend fun saveRun(
        runInput: RunInput
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val result = remoteRunDataSource.saveRun(runInput)
        if (result is Result.Success) {
            localRunDataSource.saveRun(result.value, runInput)
        } else {
            Result.Failure((result as? Result.Failure)?.throwable)
        }
    }

    override suspend fun getLocationShort(latLng: LatLng): Result<LocationShort> = withContext(Dispatchers.IO) {
        remoteRunDataSource.getLocationShort(latLng)
    }
}