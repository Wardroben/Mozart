package com.example.mozart.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mozart.data.dao.SoundDao
import com.example.mozart.domain.model.sound.SoundEntity

@Database(entities = [SoundEntity::class], version = 1, exportSchema = false)
abstract class SoundDatabase : RoomDatabase() {
    abstract fun soundDao(): SoundDao
}