package com.karyna.framework.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.karyna.framework.dto.Run
import com.karyna.framework.dto.User
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.local.dao.UserDao

@Database(entities = [User::class, Run::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RunDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
    abstract fun userDao(): UserDao
}