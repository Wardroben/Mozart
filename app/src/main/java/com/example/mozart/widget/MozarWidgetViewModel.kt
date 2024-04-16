package com.example.mozart.widget

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
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MozartWidgetViewModel @Inject constructor(repository: SoundRepository) :
    ViewModel() {
    private val _playingSoundId = MutableStateFlow<Long?>(null)
    val uiState: StateFlow<WidgetUiState> =
        combine(_playingSoundId, repository.getSoundsAtWidget()) { playingSoundId, sounds ->
            WidgetUiState(
                sounds = sounds,
                playingSoundId = playingSoundId
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = WidgetUiState()
        )

    fun setPlayingSound(soundId: Long?) {
        _playingSoundId.update {
            if (_playingSoundId.value == soundId) null else soundId
        }
    }
}


@Immutable
data class WidgetUiState(
    val sounds: List<Sound> = emptyList(),
    val playingSoundId: Long? = null
)