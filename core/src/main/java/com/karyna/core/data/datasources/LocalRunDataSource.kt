package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput

interface LocalRunDataSource {
    suspend fun getRun(id: Long): Result<Run?>
    suspend fun getRuns(userId: String): Result<List<Run>>

    suspend fun saveRun(id: String, runInput: RunInput): Result<Unit>

    suspend fun deleteRun(runId: String): Result<Unit>
}