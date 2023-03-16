package com.karyna.core.data

import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort

interface RunningRepository {
    //PERSONAL

    fun getRun(userEmail: String): Result<Run>
    //todo pagination, filtration
    fun getRunsShort(userEmail: String): Result<List<Run>>
    fun getUser(userEmail: String): Result<User>

    //SOCIAL
    fun getTopRuns(amount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>

    //MAP
    fun saveRun(run: Run): Result<Unit>
}