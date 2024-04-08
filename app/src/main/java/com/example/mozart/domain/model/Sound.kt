package com.example.mozart.domain.model

data class Sound(
    val id: Long,
    val fileName: String,
    val uri: String,
    val startPosition: Long,
    val endPosition: Long,
    val duration: Long = 0L
)

fun Sound.toEntity() = SoundEntity(id, fileName, uri, startPosition, endPosition)