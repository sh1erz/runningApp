package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort

interface RunDataSource {
    fun getRun(userEmail: String): Result<Run>
    fun getRunsShort(userEmail: String): Result<List<Run>>
    fun getTopRuns(amount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>
    fun saveRun(run: Run): Result<Unit>
}