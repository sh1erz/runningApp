package com.karyna.framework.local.dao

import androidx.room.*
import com.karyna.framework.dto.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id=:id")
    fun getUser(id: Long): User?

    @Query("SELECT * FROM user WHERE email LIKE :email")
    fun getUser(email: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)
}