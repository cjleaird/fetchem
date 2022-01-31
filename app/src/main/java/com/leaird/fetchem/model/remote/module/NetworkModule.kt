package com.leaird.fetchem.model.remote.module

import com.leaird.fetchem.model.remote.NetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun providesNetworkService() = NetworkClient.create(BASE_URL)

    companion object {
        private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    }
}