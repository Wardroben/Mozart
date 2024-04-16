package com.example.mozart.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.domain.repository.SoundRepository
import com.example.mozart.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MozartWidgetViewModel @Inject constructor(repository: SoundRepository) :
    ViewModel() {
    private val _playingSoundId = MutableStateFlow<Long?>(null)
    val sounds: StateFlow<List<Sound>> = repository.getSoundsAtWidget().stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = emptyList()
    )

    fun setPlayingSound(soundId: Long?) {
        _playingSoundId.update {
            if (_playingSoundId.value == soundId) null else soundId
        }
    }
}