package com.karyna.framework.di.remote

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
    abstract fun bindRemoteRunDatasource(
        localRunDatasource: RemoteDataSourceImpl
    ): RemoteUserDataSource
//
//    @Binds
//    abstract fun bindLocalUserDatasource(
//        localRunDatasource: LocalUserDataSourceImpl
//    ): LocalUserDataSource

}