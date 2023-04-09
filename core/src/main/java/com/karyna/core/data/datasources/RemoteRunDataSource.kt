package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput

interface RemoteRunDataSource {
    suspend fun getRun(id: String): Result<Run>
    suspend fun saveRun(runInput: RunInput): Result<String>

    //    fun getRunsShort(userId: String): Result<List<Run>>
//    fun getTopRuns(itemsAmount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>

//    fun deleteRun(runId: String): Result<Unit>
}