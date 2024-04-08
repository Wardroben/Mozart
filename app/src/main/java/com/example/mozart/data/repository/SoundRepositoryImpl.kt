package com.example.mozart.data.repository

import com.example.mozart.data.dao.SoundDao
import com.example.mozart.domain.model.Sound
import com.example.mozart.domain.model.SoundEntity
import com.example.mozart.domain.model.toEntity
import com.example.mozart.domain.model.toSound
import com.example.mozart.domain.repository.SoundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SoundRepositoryImpl @Inject constructor(
    private val dao: SoundDao
) : SoundRepository {
    override suspend fun insertSounds(sounds: List<Sound>) =
        dao.insertSounds(sounds.map { sound -> sound.toEntity() })

    override suspend fun updateSound(sound: Sound) = dao.updateSound(sound.toEntity())

    override suspend fun deleteSounds(sounds: List<Sound>) =
        dao.deleteSounds(sounds.map { sound -> sound.toEntity() })

    override fun getAllSounds(): Flow<List<Sound>> =
        dao.getAllSounds().map { sounds: List<SoundEntity> ->
            sounds.map { soundEntity ->
                soundEntity.toSound()
            }
        }
}