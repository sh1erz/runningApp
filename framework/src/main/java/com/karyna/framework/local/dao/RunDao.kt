package com.karyna.framework.local.dao

import androidx.room.*
import com.karyna.framework.dto.LocalRun

@Dao
interface RunDao {
    @Query("SELECT * FROM run WHERE id=:id")
    fun getRun(id: Long): LocalRun?

    @Query("SELECT * FROM run WHERE userId=:userId")
    fun getRuns(userId: String): List<LocalRun>

    @Insert
    fun insertRun(localRun: LocalRun)

    @Delete
    fun deleteRun(localRun: LocalRun)
}