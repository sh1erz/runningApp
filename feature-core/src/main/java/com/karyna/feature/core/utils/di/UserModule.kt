package com.karyna.feature.core.utils.di

import com.google.firebase.auth.FirebaseAuth
import com.karyna.core.domain.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ServiceComponent::class)
class UserModule {
    @Provides
    fun provideUser(): User = FirebaseAuth.getInstance().currentUser?.run {
        if (email != null && displayName != null && photoUrl != null) {
            User(
                id = uid,
                email = email!!,
                name = displayName!!,
                avatarUrl = photoUrl!!.toString(),
                weight = null
            )
        } else null
    } ?: throw IllegalStateException("Current user is null")
}


@Module
@InstallIn(ViewModelComponent::class)
class UserModuleVM {
    @Provides
    fun provideUser(): User = FirebaseAuth.getInstance().currentUser?.run {
        if (email != null && displayName != null && photoUrl != null) {
            User(
                id = uid,
                email = email!!,
                name = displayName!!,
                avatarUrl = photoUrl!!.toString(),
                weight = null
            )
        } else null
    } ?: throw IllegalStateException("Current user is null")
}