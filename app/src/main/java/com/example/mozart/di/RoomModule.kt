package com.example.mozart.di

import android.content.Context
import androidx.room.Room
import com.example.mozart.data.SoundDatabase
import com.example.mozart.data.dao.SoundDao
import com.example.mozart.data.repository.SoundRepositoryImpl
import com.example.mozart.domain.repository.SoundRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SoundDatabase = Room.databaseBuilder(
        context,
        SoundDatabase::class.java,
        "SoundDatabase"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideSoundDao(database: SoundDatabase): SoundDao = database.soundDao()

    @Provides
    @Singleton
    fun provideSoundRepository(dao: SoundDao): SoundRepository = SoundRepositoryImpl(dao)
}