package com.example.mozart.presentation.sounds_grid

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.domain.repository.SoundRepository
import com.example.mozart.presentation.sounds_grid.SoundFilterType.ALL_SOUNDS
import com.example.mozart.presentation.sounds_grid.SoundFilterType.ON_WIDGET_SOUNDS
import com.example.mozart.presentation.sounds_grid.components.SheetActionGroup
import com.example.mozart.util.Async
import com.example.mozart.util.WhileUiSubscribed
import com.example.mozart.widget.MozartWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundsViewModel @Inject constructor(
    private val repository: SoundRepository
) : ViewModel() {
    private val _userMessage = MutableStateFlow<Int?>(null)
    private val _filterType = MutableStateFlow<SoundFilterType>(ALL_SOUNDS)
    private val _filterUiInfo = _filterType.map { getFilterUiInfo(it) }
    private val _filteredSoundsAsync =
        combine(repository.getAllSounds(), _filterType) { sounds, type ->
            filterSounds(sounds, type)
        }
            .map { Async.Success(it) }
            .catch<Async<List<Sound>>> { emit(Async.Error(R.string.error_while_loading_sounds)) }

    private val _sheetActionGroup = MutableStateFlow<SheetActionGroup>(SheetActionGroup.None)
    val modalSheetState = _sheetActionGroup.map { actionGroup ->
        ModalSheetState(actionGroup = actionGroup)
    }

    private val _selectedSounds = MutableStateFlow<List<Sound>>(emptyList())
    private val _playingSoundId = MutableStateFlow<Long?>(null)
    val uiState: StateFlow<SoundsUiState> = combine(
        _filterUiInfo, _filteredSoundsAsync, _selectedSounds, _userMessage, _playingSoundId
    ) { filterUiInfo, soundsAsync, selectedSounds, userMessage, playingSoundId ->
        when (soundsAsync) {
            Async.Loading -> SoundsUiState(isLoading = true)
            is Async.Error -> SoundsUiState(userMessage = userMessage)
            is Async.Success -> SoundsUiState(
                sounds = soundsAsync.data,
                filterUiInfo = filterUiInfo,
                selectedSounds = selectedSounds,
                playingSoundId = playingSoundId
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = SoundsUiState()
    )

    fun addSounds(sounds: List<Sound>) = viewModelScope.launch {
        repository.insertSounds(sounds)
    }

    fun deleteSounds(sounds: List<Sound>) = viewModelScope.launch {
        repository.deleteSounds(sounds)
    }

    fun setFiltering(filteringType: SoundFilterType) {
        _filterType.update {
            filteringType
        }
    }

    fun moveSoundWidget(sound: Sound, atWidget: Boolean, context: Context) {
        viewModelScope.launch {
            repository.updateSound(
                sound.copy(atWidget = atWidget)
            )
            MozartWidget().updateAll(context)
        }
    }

    fun changeSoundSelection(sound: Sound) = viewModelScope.launch {
        _selectedSounds.update { sounds ->
            if (sounds.contains(sound)) sounds.minus(sound)
            else sounds.plus(sound)
        }
    }

    fun playSound(soundId: Long?) {
        _playingSoundId.update {
            soundId
        }
    }
    fun snackbarMessageShown() {
        _userMessage.update { null }
    }

    fun showFilterOptions() {
        _sheetActionGroup.update {
            SheetActionGroup.Filter
        }
    }

    fun showSoundControls(sound: Sound) {
        val soundIndex = uiState.value.sounds.indexOf(sound)
        val soundToControl = uiState.value.sounds[soundIndex]
        _sheetActionGroup.update {
            SheetActionGroup.SoundControl(soundToControl)
        }
    }

    fun hideBottomModalSheet() {
        _sheetActionGroup.update {
            SheetActionGroup.None
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.update { message }
    }

    private fun filterSounds(sounds: List<Sound>, filteringType: SoundFilterType): List<Sound> {
        return when (filteringType) {
            ALL_SOUNDS -> sounds
            ON_WIDGET_SOUNDS -> sounds.filter { sound -> sound.atWidget }
        }
    }

    private fun getFilterUiInfo(requestType: SoundFilterType): FilteringUiInfo =
        when (requestType) {
            ALL_SOUNDS -> {
                FilteringUiInfo(
                    currentFilteringLabel = R.string.label_all_sounds,
                    noSoundsLabel = R.string.no_sounds_all
                )
            }

            ON_WIDGET_SOUNDS -> {
                FilteringUiInfo(
                    currentFilteringLabel = R.string.label_at_widget,
                    noSoundsLabel = R.string.no_sounds_at_widget
                )
            }
        }
}

/**
 *Ui State for the sound list screen
 */
@Immutable
data class SoundsUiState(
    val sounds: List<Sound> = emptyList(),
    val filterUiInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null,
    val playingSoundId: Long? = null,
    val selectedSounds: List<Sound> = emptyList(),
    val isLoading: Boolean = false,
)

@Immutable
data class FilteringUiInfo(
    val currentFilteringLabel: Int = R.string.label_all_sounds,
    val noSoundsLabel: Int = R.string.no_sounds_all,
    val noSoundIconRes: Int = R.drawable.baseline_error_outline_24
)

@Immutable
data class ModalSheetState(
    val actionGroup: SheetActionGroup = SheetActionGroup.None,
    val isVisible: Boolean = actionGroup !is SheetActionGroup.None
)

@Immutable
data class ModalSheetAction(
    val iconRes: Int,
    val label: Int,
    val contentDescription: Int,
    val type: ActionType
)

