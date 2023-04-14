package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput

interface RemoteRunDataSource {
    suspend fun getRun(id: String): Result<Run>
    suspend fun getRuns(userId: String): Result<List<Run>>
    suspend fun saveRun(runInput: RunInput): Result<String>
    suspend fun getLocationShort(latLng: LatLng): Result<LocationShort>

    //    fun getRunsShort(userId: String): Result<List<Run>>
//    fun getTopRuns(itemsAmount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>

//    fun deleteRun(runId: String): Result<Unit>
}