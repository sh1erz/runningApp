package com.karyna.framework.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.karyna.framework.dto.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id=:id")
    fun getUser(id: Long): User?

    @Query("SELECT * FROM user WHERE email LIKE :email")
    fun getUser(email: String): User?

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)
}