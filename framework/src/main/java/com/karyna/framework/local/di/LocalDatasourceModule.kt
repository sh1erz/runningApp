package com.karyna.framework.local.di

import com.karyna.core.data.datasources.RunDataSource
import com.karyna.framework.local.LocalRunDataSource
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

}