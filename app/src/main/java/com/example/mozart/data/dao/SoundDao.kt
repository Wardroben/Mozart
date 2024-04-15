package com.example.mozart.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mozart.domain.model.sound.SoundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundDao {
    @Insert
    suspend fun insertSounds(sounds: List<SoundEntity>)

    @Update
    suspend fun updateSound(sound: SoundEntity)

    @Delete
    suspend fun deleteSounds(sounds: List<SoundEntity>)

    @Query("SELECT * FROM sounds")
    fun getAllSounds(): Flow<List<SoundEntity>>

}