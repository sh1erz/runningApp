package com.karyna.framework.local.dao

import androidx.room.*
import com.karyna.framework.dto.Run

@Dao
interface RunDao {
    @Query("SELECT * FROM run WHERE id=:id")
    fun getRun(id: Long): Run?

    @Query("SELECT * FROM run WHERE userId=:userId")
    fun getRunsShort(userId: String): List<Run>

    @Insert
    fun insertRun(run: Run)

    @Delete
    fun deleteRun(run: Run)
}