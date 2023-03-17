package com.karyna.core.data.di

import com.karyna.core.data.RunningRepository
import com.karyna.core.data.RunningRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(
        repositoryImpl: RunningRepositoryImpl
    ): RunningRepository
}