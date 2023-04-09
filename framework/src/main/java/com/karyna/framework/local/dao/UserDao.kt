package com.karyna.framework.local.dao

import androidx.room.*
import com.karyna.framework.dto.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id LIKE '%' || :id || '%'")
    fun getUser(id: String): User?

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)
}