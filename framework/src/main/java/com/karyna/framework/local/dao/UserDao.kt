package com.karyna.framework.local.dao

import androidx.room.*
import com.karyna.framework.dto.LocalUser

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id LIKE '%' || :id || '%'")
    fun getUser(id: String): LocalUser?

    @Insert
    fun insertUser(user: LocalUser)

    @Delete
    fun deleteUser(user: LocalUser)

    @Query("UPDATE user SET weight = :weight WHERE id LIKE '%' || :id || '%'")
    fun updateWeight(id: String, weight: Float?)
}