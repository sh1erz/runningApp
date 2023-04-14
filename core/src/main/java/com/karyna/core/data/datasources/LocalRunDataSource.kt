package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
import com.karyna.core.domain.run.RunShort

interface LocalRunDataSource {
    suspend fun getRun(id: Long): Result<Run?>
    suspend fun getRuns(userId: String): Result<List<Run>>
    suspend fun getTopRuns(itemsAmount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>
    suspend fun saveRun(id: String, runInput: RunInput): Result<Unit>

    suspend fun deleteRun(runId: String): Result<Unit>
}