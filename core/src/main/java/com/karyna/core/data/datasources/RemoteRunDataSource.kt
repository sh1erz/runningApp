package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.run.OrderingMode
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput

interface RemoteRunDataSource {
    suspend fun getRun(id: String): Result<Run>
    suspend fun getRuns(userId: String): Result<List<Run>>
    suspend fun getTopRuns(
        amount: Int,
        ordering: OrderingMode,
        isoDateFrom: String,
        isoDateTo: String,
    ): Result<List<Run>>

    suspend fun saveRun(runInput: RunInput): Result<String>
    suspend fun getLocationShort(latLng: LatLng): Result<LocationShort>
//    fun deleteRun(runId: String): Result<Unit>
}