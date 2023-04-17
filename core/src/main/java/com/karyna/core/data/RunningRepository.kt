package com.karyna.core.data

import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User
import com.karyna.core.domain.run.OrderingMode
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput

interface RunningRepository {
    //AUTH
    suspend fun addUser(user: User, addToRemote: Boolean): Result<Unit>

    //PERSONAL
    suspend fun getRun(userId: String): Result<Run>

    //todo pagination, filtration
    suspend fun getRuns(userId: String): Result<List<Run>>

    //SOCIAL
    suspend fun getUser(userId: String): Result<User>
    suspend fun getTopRuns(
        amount: Int,
        ordering: OrderingMode,
        isoDateFrom: String,
        isoDateToExcl: String
    ): Result<List<Run>>

    //MAP
    suspend fun saveRun(runInput: RunInput): Result<Unit>

    suspend fun getLocationShort(latLng: LatLng): Result<LocationShort>

}