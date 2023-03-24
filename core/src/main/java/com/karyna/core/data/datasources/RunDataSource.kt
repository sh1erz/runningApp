package com.karyna.core.data.datasources

import com.karyna.core.data.Result
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunShort

interface RunDataSource {
    fun getRun(id: Long): Result<Run?>
    fun getRunsShort(userEmail: String): Result<List<Run>>
    fun getTopRuns(itemsAmount: Int, lastDays: Int?, country: String?): Result<List<RunShort>>
    fun saveRun(
        userEmail: String,
        date: String,
        location: LocationShort,
        coordinates: List<LatLng>,
        durationS: Long,
        distanceMeters: Int,
        paceMetersInS: Int,
        calories: Int?
    ): Result<Unit>

    fun deleteRun(runId: String): Result<Unit>
}