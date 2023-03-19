package com.karyna.core.data

import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort

interface RunningRepository {
    suspend fun addUser(user: User): Result<Unit>

    //PERSONAL
    suspend fun getRun(userEmail: String): Result<Run>

    //todo pagination, filtration
    suspend fun getRunsShort(userEmail: String): Result<List<Run>>
    suspend fun getUser(userEmail: String): Result<User>

    //SOCIAL
    suspend fun getTopRuns(amount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>

    //MAP
    suspend fun saveRun(run: Run): Result<Unit>
}