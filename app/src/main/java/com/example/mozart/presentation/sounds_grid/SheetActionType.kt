package com.example.mozart.presentation.sounds_grid

import com.example.mozart.domain.model.sound.Sound

sealed interface ActionType {
    sealed class Filter : ActionType
    sealed class Control : ActionType
}

sealed class FilterSound : ActionType.Filter() {
    data object OnWidget : FilterSound()
    data object AllSounds : FilterSound()
}

sealed class ControlSound : ActionType.Control() {
    data class Delete(val sound: Sound): ControlSound()
    data class Edit(val sound: Sound): ControlSound()
    data class AddOnWidget(val sound: Sound): ControlSound()
    data class RemoveFromWidget(val sound: Sound): ControlSound()
}


