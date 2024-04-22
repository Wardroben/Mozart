package com.example.mozart.widget

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.domain.repository.SoundRepository
import com.example.mozart.util.Async
import com.example.mozart.util.WhileUiSubscribed
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MozartWidgetViewModel @Inject constructor(repository: SoundRepository) :
    ViewModel() {
    val controller = mutableStateOf<MediaController?>(null)

    //val controller = _controller.asStateFlow()
    private val _controllerFuture = mutableStateOf<ListenableFuture<MediaController>?>(null)
    private val _errorMessage = MutableStateFlow<Int?>(null)
    private val _sounds = repository.getSoundsAtWidget()
        .map { Async.Success(it) }
        .catch<Async<List<Sound>>> { emit(Async.Error(R.string.error_while_loading_sounds)) }
    val sounds: StateFlow<WidgetUiState> = combine(
        _sounds,
        _errorMessage
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

    fun setControllerFuture(controllerFuture: ListenableFuture<MediaController>) {
        _controllerFuture.value = controllerFuture

        controllerFuture.addListener(
            {
                controller.value = controllerFuture.get()
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun onCleared() {
        _controllerFuture.value?.let {
            MediaController.releaseFuture(it)
        }
        super.onCleared()
    }
}

@Immutable
data class WidgetUiState(
    val sounds: List<Sound> = emptyList(),
    val errorMessage: Int? = null,
    val isLoading: Boolean = true,
)