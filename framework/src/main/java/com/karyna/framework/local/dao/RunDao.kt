package com.karyna.framework.local.dao

import androidx.room.*
import com.karyna.framework.dto.LocalRun

@Dao
interface RunDao {
    @Query("SELECT * FROM run WHERE id LIKE '%' || :id || '%'")
    fun getRun(id: String): LocalRun?

    @Query("SELECT * FROM run WHERE userId LIKE '%' || :userId || '%'")
    fun getRuns(userId: String): List<LocalRun>

    @Insert
    fun insertRun(localRun: LocalRun)

    @Query("DELETE FROM run WHERE id LIKE '%' || :id || '%'")
    fun deleteRun(id: String)
}