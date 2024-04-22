package com.example.mozart.widget

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.domain.repository.SoundRepository
import com.example.mozart.util.Async
import com.example.mozart.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MozartWidgetViewModel @Inject constructor(repository: SoundRepository) :
    ViewModel() {
    private val _playingSoundId = MutableStateFlow<Long?>(null)
    private val _errorMessage = MutableStateFlow<Int?>(null)
    private val _sounds = repository.getSoundsAtWidget()
        .map { Async.Success(it) }
        .catch<Async<List<Sound>>> { emit(Async.Error(R.string.error_while_loading_sounds)) }
    val sounds: StateFlow<WidgetUiState> = combine(
        _sounds,
        _errorMessage,
    ) { soundsAsync, errorMessage ->
        when (soundsAsync) {
            is Async.Error -> WidgetUiState(errorMessage = errorMessage)
            Async.Loading -> WidgetUiState(isLoading = true)
            is Async.Success -> WidgetUiState(
                sounds = soundsAsync.data,
                isLoading = false
            )
        }
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
    val errorMessage: Int? = null,
    val isLoading: Boolean = true,
)