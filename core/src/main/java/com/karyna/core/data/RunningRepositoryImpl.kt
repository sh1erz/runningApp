package com.karyna.core.data

import com.karyna.core.data.datasources.RunDataSource
import com.karyna.core.data.datasources.UserDataSource
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunningRepositoryImpl @Inject constructor(
    private val localRunDataSource: RunDataSource,
    private val localUserDataSource: UserDataSource
//    remoteRunDataSource: RunDataSource,
) : RunningRepository {
    override suspend fun addUser(user: User): Result<Unit> = withContext(Dispatchers.IO) {
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

    override suspend fun saveRun(run: Run): Result<Unit> = withContext(Dispatchers.IO) {
        localRunDataSource.saveRun(run)
    }
}