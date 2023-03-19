package com.karyna.framework.di.local

import com.karyna.core.data.datasources.RunDataSource
import com.karyna.core.data.datasources.UserDataSource
import com.karyna.framework.local.datasources.LocalRunDataSource
import com.karyna.framework.local.datasources.LocalUserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDatasourceModule {

    @Binds
    abstract fun bindLocalRunDatasource(
        localRunDatasource: LocalRunDataSource
    ): RunDataSource

    @Binds
    abstract fun bindLocalUserDatasource(
        localRunDatasource: LocalUserDataSource
    ): UserDataSource

}