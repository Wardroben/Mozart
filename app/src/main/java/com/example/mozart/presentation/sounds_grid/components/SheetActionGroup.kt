package com.example.mozart.presentation.sounds_grid.components

import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.presentation.sounds_grid.ModalSheetAction
import com.example.mozart.presentation.sounds_grid.SheetActions

sealed class SheetActionGroup(val actions: List<ModalSheetAction>) {
    data object Filter : SheetActionGroup(SheetActions.filterActions)
    data class SoundControl(val sound: Sound) :
        SheetActionGroup(SheetActions.soundControlActions(sound))

    data object None : SheetActionGroup(emptyList())
}
