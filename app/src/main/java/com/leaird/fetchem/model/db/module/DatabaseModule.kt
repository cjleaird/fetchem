package com.leaird.fetchem.model.db.module

import android.content.Context
import androidx.room.Room
import com.leaird.fetchem.model.db.FetchEmDatabase
import com.leaird.fetchem.model.db.PuppyItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FetchEmDatabase {
        return Room.databaseBuilder(
            appContext,
            FetchEmDatabase::class.java,
            FetchEmDatabase.DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePuppyItemDao(database: FetchEmDatabase): PuppyItemDao {
        return database.puppyItemDao()
    }
}