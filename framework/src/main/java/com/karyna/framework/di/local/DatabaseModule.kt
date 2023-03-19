package com.karyna.framework.di.local

import android.content.Context
import androidx.room.Room
import com.karyna.framework.local.RunDatabase
import com.karyna.framework.local.dao.RunDao
import com.karyna.framework.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideRunDao(db: RunDatabase): RunDao {
        return db.runDao()
    }

    @Provides
    fun provideUserDao(db: RunDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideRunDatabase(@ApplicationContext appContext: Context): RunDatabase {
        return Room.databaseBuilder(
            appContext,
            RunDatabase::class.java,
            RunDatabase.DB_NAME
        ).build()
    }
}