package com.example.mozart.util

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis = 5_000L

val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)