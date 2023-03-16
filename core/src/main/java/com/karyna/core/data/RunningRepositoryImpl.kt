package com.karyna.core.data

import com.karyna.core.data.datasources.RunDataSource
import com.karyna.core.data.datasources.UserDataSource
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort
import javax.inject.Inject

class RunningRepositoryImpl @Inject constructor(
    localRunDataSource: RunDataSource,
    remoteRunDataSource: RunDataSource,
    remoteUserDataSource: UserDataSource
) : RunningRepository {
    override fun getRun(userEmail: String): Result<Run> {
        TODO("Not yet implemented")
    }

    override fun getRunsShort(userEmail: String): Result<List<Run>> {
        TODO("Not yet implemented")
    }

    override fun getUser(userEmail: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun getTopRuns(amount: Int, lastDays: Int?, country: String?): Result<List<RunShort>> {
        TODO("Not yet implemented")
    }

    override fun saveRun(run: Run): Result<Unit> {
        TODO("Not yet implemented")
    }
}