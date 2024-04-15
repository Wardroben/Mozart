package com.example.mozart.presentation.widget

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.domain.repository.SoundRepository
import com.example.mozart.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MozartWidgetViewModel @Inject constructor(private val repository: SoundRepository) :
    ViewModel() {
    private val _playingSound = MutableStateFlow<Sound?>(null)
    val uiState: StateFlow<WidgetUiState> =
        combine(_playingSound, repository.getSoundsAtWidget()) { playingSound, sounds ->
        WidgetUiState(
            sounds = sounds,
            playingSound = playingSound
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = WidgetUiState()
    )
}

@Immutable
data class WidgetUiState(
    val sounds: List<Sound> = emptyList(),
    val playingSound: Sound? = null
)