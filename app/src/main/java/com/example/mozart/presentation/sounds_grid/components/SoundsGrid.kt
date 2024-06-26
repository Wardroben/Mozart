package com.example.mozart.presentation.sounds_grid.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.util.SoundExamples

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundsGrid(
    modifier: Modifier = Modifier,
    sounds: List<Sound>,
    playingSoundId: Long?,
    onSoundClicked: (Sound) -> Unit,
    onLongSoundClicked: (Sound) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.list_item_padding)),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.list_item_padding),
            alignment = Alignment.Bottom
        ),
        contentPadding = PaddingValues(horizontal = 5.dp),
        reverseLayout = true
    ) {
        items(items = sounds, key = { sound -> sound.id }) { sound ->
            SoundItem(
                sound = sound,
                isPlaying = playingSoundId == sound.id,
                modifier = Modifier.animateItemPlacement(),
                onClick = { onSoundClicked(sound) },
                onLongClicked = {
                    onLongSoundClicked(sound)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }

            )
        }
    }
}

@Preview(widthDp = 250, heightDp = 400)
@Composable
private fun SoundsGridPreview() {
    SoundsGrid(
        sounds = SoundExamples.sounds,
        playingSoundId = null,
        onSoundClicked = {},
        onLongSoundClicked = {})
}