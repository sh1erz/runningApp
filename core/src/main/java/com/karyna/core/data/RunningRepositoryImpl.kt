package com.karyna.core.data

import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.data.datasources.LocalUserDataSource
import com.karyna.core.data.datasources.RemoteRunDataSource
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User
import com.karyna.core.domain.run.OrderingMode
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
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

    override suspend fun setWeight(userId: String, weight: Float?): Result<Unit> = withContext(Dispatchers.IO) {
        val response = remoteUserDataSource.setWeight(userId, weight)
        val localResult = localUserDataSource.setWeight(userId, weight)
        if (response.isSuccess && localResult.isSuccess) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun getRuns(userId: String): Result<List<Run>> = withContext(Dispatchers.IO) {
        val response = remoteRunDataSource.getRuns(userId)
        if (response.isFailure) {
            localRunDataSource.getRuns(userId)
        } else response
    }

    override suspend fun deleteRun(runId: String): Result<Unit> = withContext(Dispatchers.IO) {
        val remoteResult = remoteRunDataSource.deleteRun(runId)
        val localResult = localRunDataSource.deleteRun(runId)
        if (remoteResult.isFailure || localResult.isFailure) {
            Result.failure(remoteResult.exceptionOrNull() ?: localResult.exceptionOrNull() ?: Exception())
        } else remoteResult
    }

    override suspend fun getUser(userId: String): Result<User> = withContext(Dispatchers.IO) {
        val result = remoteUserDataSource.getUser(userId)
        if (result.isSuccess) {
            result
        } else localUserDataSource.getUser(userId)
    }

    override suspend fun getTopRuns(
        amount: Int,
        ordering: OrderingMode,
        isoDateFrom: String,
        isoDateToExcl: String
    ): Result<List<Run>> = withContext(Dispatchers.IO) {
        remoteRunDataSource.getTopRuns(amount, ordering, isoDateFrom, isoDateToExcl)
    }

    override suspend fun saveRun(
        runInput: RunInput
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val result = remoteRunDataSource.saveRun(runInput)
        if (result.isSuccess) {
            localRunDataSource.saveRun(result.getOrThrow(), runInput)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception())
        }
    }

    override suspend fun getLocationShort(latLng: LatLng): Result<LocationShort> = withContext(Dispatchers.IO) {
        remoteRunDataSource.getLocationShort(latLng)
    }
}