package com.karyna.framework.di.local

import com.karyna.core.data.datasources.LocalRunDataSource
import com.karyna.core.data.datasources.LocalUserDataSource
import com.karyna.framework.local.datasources.LocalRunDataSourceImpl
import com.karyna.framework.local.datasources.LocalUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDatasourceModule {

    @Binds
    abstract fun bindLocalRunDatasource(
        localRunDatasource: LocalRunDataSourceImpl
    ): LocalRunDataSource

    @Binds
    abstract fun bindLocalUserDatasource(
        localRunDatasource: LocalUserDataSourceImpl
    ): LocalUserDataSource

}