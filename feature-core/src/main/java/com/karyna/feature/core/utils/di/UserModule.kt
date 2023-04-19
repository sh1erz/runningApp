package com.karyna.feature.core.utils.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
class UserModule {
    @Provides
    @ServiceScoped
    @Named("userId")
    fun provideUserId(): String =
        FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("Current user is null")
}


@Module
@InstallIn(ViewModelComponent::class)
class UserModuleVM {
    @Provides
    @ViewModelScoped
    @Named("userId")
    fun provideUserId(): String =
        FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("Current user is null")
}