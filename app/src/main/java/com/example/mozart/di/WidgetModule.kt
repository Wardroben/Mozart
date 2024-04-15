package com.example.mozart.di

import com.example.mozart.domain.repository.SoundRepository
import com.example.mozart.presentation.widget.MozartWidgetViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/*@Module
@InstallIn(SingletonComponent::class)
object WidgetModule {
    @Provides
    @Singleton
    fun provideSoundRepository()
}*/

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint{
    fun provideSoundRepository(): SoundRepository
    fun provideViewModel() : MozartWidgetViewModel
}