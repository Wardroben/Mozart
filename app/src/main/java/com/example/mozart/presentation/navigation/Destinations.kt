package com.example.mozart.presentation.navigation

sealed class MozartDestinations(val route: String) {
    data object Sounds : MozartDestinations("sounds_screen")
    data object EditSound : MozartDestinations("sound_edit_screen")
}