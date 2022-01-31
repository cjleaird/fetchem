package com.leaird.fetchem.login.module

import com.google.firebase.auth.FirebaseAuth
import com.leaird.fetchem.login.LoginProvider
import com.leaird.fetchem.login.LoginProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {
    @Suppress("unused")
    @Binds
    abstract fun bindsLoginSource(loginProvider: LoginProviderImpl): LoginProvider

    companion object {
        @Provides
        fun providesLoginSourceImpl() = LoginProviderImpl(FirebaseAuth.getInstance())
    }
}