package com.karyna.framework.di.remote

import com.karyna.core.data.datasources.RemoteRunDataSource
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.framework.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDatasourceModule {

    @Binds
    abstract fun bindRemoteUserDatasource(
        ds: RemoteDataSourceImpl
    ): RemoteUserDataSource

    @Binds
    abstract fun bindRemoteRunDatasource(
        ds: RemoteDataSourceImpl
    ): RemoteRunDataSource

}