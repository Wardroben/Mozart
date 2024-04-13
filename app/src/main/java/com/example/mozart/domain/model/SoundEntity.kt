package com.example.mozart.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sounds")
data class SoundEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "file_name") val fileName: String,
    val uri: String,
    @ColumnInfo(name = "start_position") val startPosition: Long,
    @ColumnInfo(name = "end_position") val endPosition: Long?,
    @ColumnInfo(name = "at_widget") val atWidget: Boolean = false
    //@Ignore val duration: Long
)

fun SoundEntity.toSound() =
    Sound(id, fileName, uri, startPosition, endPosition, atWidget = atWidget)