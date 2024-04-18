package com.example.mozart.presentation.sounds_grid

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.mediasession.rememberManagedMediaController
import com.example.mozart.presentation.sounds_grid.components.AddSoundsFAB
import com.example.mozart.presentation.sounds_grid.components.LoadingContent
import com.example.mozart.presentation.sounds_grid.components.ModalSheetContent
import com.example.mozart.presentation.sounds_grid.components.SoundsBottomAppBarActions
import com.example.mozart.presentation.sounds_grid.components.SoundsEmptyContent
import com.example.mozart.presentation.sounds_grid.components.SoundsGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundGridScreen(
    modifier: Modifier = Modifier,
    viewModel: SoundsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val mediaController by rememberManagedMediaController()

    mediaController?.addListener(object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            if (events.contains(Player.EVENT_TRACKS_CHANGED)) {
                val soundId = player.currentMediaItem?.mediaId?.toLong()
                viewModel.playSound(soundId)
            }
        }
    })
    viewModel.playSound(mediaController?.currentMediaItem?.mediaId?.toLong())

    val soundsLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenMultipleDocuments()
        ) { uris: List<Uri> ->
            takePersistablePermissionForUri(uris, context)
            val filenames = uris.map { uri -> dumpFileDisplayName(uri, context.contentResolver) }
            val sounds = mapUrisToSounds(uris, filenames)
            viewModel.addSounds(sounds)
        }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomAppBar(
                actions = {
                    //TODO actionGroup: sorting, finding...
                    SoundsBottomAppBarActions(
                        onSearch = { /*TODO*/ },
                        onSort = { viewModel.showFilterOptions() }
                    )
                },
                floatingActionButton = {
                    AddSoundsFAB(onClick = { soundsLauncher.launch(arrayOf("audio/*")) })
                }
            )
        }
    ) { paddings ->
        val uiState by viewModel.uiState.collectAsState()
        val modalSheetState by viewModel.modalSheetState.collectAsState(ModalSheetState())
        SoundsContent(
            modifier = modifier.padding(paddings),
            loading = uiState.isLoading,
            currentFilteringLabel = uiState.filterUiInfo.currentFilteringLabel,
            noSoundsLabel = uiState.filterUiInfo.noSoundsLabel,
            noSoundsIconRes = uiState.filterUiInfo.noSoundIconRes,
            sounds = uiState.sounds,
            playingSoundId = uiState.playingSoundId,
            onSoundClicked = { sound ->
                if (mediaController?.isPlaying == false) {
                    mediaController?.run {
                        setMediaItem(getMediaItemFromSound(sound))
                        prepare()
                        play()
                    }
                } else if (mediaController?.currentMediaItem?.mediaId?.toLong() == sound.id) {
                    mediaController?.run {
                        stop()
                        clearMediaItems()
                    }
                } else {
                    mediaController?.run {
                        setMediaItem(getMediaItemFromSound(sound))
                        play()
                    }
                }
            },
            onLongSoundClicked = { sound -> viewModel.showSoundControls(sound = sound) }
        )

        // Check for user messages to display on the screen
        uiState.userMessage?.let { message ->
            val snackbarText = stringResource(id = message)
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(snackbarText, duration = SnackbarDuration.Short)
                viewModel.snackbarMessageShown()
            }
        }
        if (modalSheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideBottomModalSheet() },
                sheetState = sheetState
            ) {
                ModalSheetContent(
                    actionGroup = modalSheetState.actionGroup,
                    filterSounds = { filterType -> viewModel.setFiltering(filterType) },
                    //addSoundToWidget = { viewModel.moveSoundWidget(it, true) },
                    //removeSoundFromWidget = { viewModel.moveSoundWidget(it, false) },
                    changeSoundWidgetState = { sound, atWidget ->
                        viewModel.moveSoundWidget(sound, atWidget, context)
                    },
                    onDeleteSound = { viewModel.deleteSounds(listOf(it)) },
                    onEditSound = { TODO() },
                    hideSheet = {
                        scope.launch { sheetState.hide() }
                            .invokeOnCompletion { viewModel.hideBottomModalSheet() }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }
}

private fun getMediaItemFromSound(sound: Sound): MediaItem =
    MediaItem.Builder()
        .setMediaId(sound.id.toString())
        .setUri(sound.uri)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(sound.fileName)
                .build()
        ).build()

@Composable
fun SoundsContent(
    modifier: Modifier = Modifier,
    loading: Boolean,
    @StringRes currentFilteringLabel: Int,
    @StringRes noSoundsLabel: Int,
    @DrawableRes noSoundsIconRes: Int,
    sounds: List<Sound>,
    playingSoundId: Long?,
    onLongSoundClicked: (Sound) -> Unit,
    onSoundClicked: (Sound) -> Unit,
) {
    LoadingContent(
        loading = loading,
        empty = sounds.isEmpty() && !loading,
        emptyContent = {
            SoundsEmptyContent(
                noSoundsLabel = noSoundsLabel,
                noSoundsIcon = noSoundsIconRes
            )
        }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = currentFilteringLabel),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
                style = MaterialTheme.typography.labelSmall
            )
            SoundsGrid(
                sounds = sounds,
                playingSoundId = playingSoundId,
                onLongSoundClicked = onLongSoundClicked,
                onSoundClicked = onSoundClicked
            )
        }
    }
}

/*
* map uri list and file names list to sound
 */
private fun mapUrisToSounds(
    uris: List<Uri>,
    fileNames: List<String>
) = uris.mapIndexed { index, uri ->
    Sound(
        uri = uri.toString(),
        fileName = fileNames[index]
    )
}

private fun takePersistablePermissionForUri(
    uris: List<Uri>,
    context: Context
) {
    uris.forEach { uri ->
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}

private fun dumpFileDisplayName(uri: Uri, contentResolver: ContentResolver): String {
    val cursor: Cursor? = contentResolver.query(
        uri,
        null,
        null,
        null,
        null,
        null
    )
    var displayName = String()
    cursor?.use {
        displayName = if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.getString(nameIndex)
        } else "Unknown"
    }
    return displayName
}