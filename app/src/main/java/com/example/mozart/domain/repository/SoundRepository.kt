package com.example.mozart.domain.repository

import com.example.mozart.domain.model.sound.Sound
import kotlinx.coroutines.flow.Flow

interface SoundRepository {
    suspend fun insertSounds(sounds: List<Sound>)
    suspend fun updateSound(sound: Sound)
    suspend fun deleteSounds(sounds: List<Sound>)

    fun getAllSounds(): Flow<List<Sound>>

}