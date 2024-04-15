package com.example.mozart.domain.model.sound

data class Sound(
    val id: Long = 0L,
    val fileName: String,
    val uri: String,
    val startPosition: Long = 0L,
    val endPosition: Long? = null,
    val duration: Long = 0L,
    val atWidget: Boolean = false
)

fun Sound.toEntity() = SoundEntity(id, fileName, uri, startPosition, endPosition, atWidget)