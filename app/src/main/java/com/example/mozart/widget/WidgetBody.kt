package com.example.mozart.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.mediasession.rememberManagedMediaController
import com.example.mozart.presentation.MainActivity

@Composable
fun WidgetContent(
    modifier: GlanceModifier = GlanceModifier,
    sounds: List<Sound>,
    onPlayClick: (Sound) -> Unit,
    playingSoundId: Long?
) {
    if (sounds.isEmpty()) EmptyWidgetSounds()
    else WidgetSoundGrid(
        sounds = sounds,
        modifier = modifier.fillMaxSize().padding(10.dp),
        onSoundClick = { sound -> onPlayClick(sound) },
        playingSoundId = playingSoundId
    )
}


@Composable
fun WidgetSoundGrid(
    modifier: GlanceModifier = GlanceModifier,
    sounds: List<Sound>,
    onSoundClick: (Sound) -> Unit,
    playingSoundId: Long?
) {
    LazyVerticalGrid(
        gridCells = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = sounds, itemId = { it.id }) { sound ->
            WidgetSoundItem(
                title = sound.fileName.substringBeforeLast('.'),
                modifier = GlanceModifier.padding(15.dp),
                onClick = { onSoundClick(sound) },
                isPlaying = sound.id == playingSoundId
            )
        }
    }
}

@Composable
fun WidgetSoundItem(
    modifier: GlanceModifier = GlanceModifier,
    title: String,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = modifier.clickable { onClick() }, contentAlignment = Alignment.Center) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(
                    resId = if (isPlaying) R.drawable.baseline_stop_circle_24
                    else R.drawable.baseline_play_circle_24
                ),
                contentDescription = null
            )
            Text(
                text = title,
                maxLines = 2,
                style = TextStyle(GlanceTheme.colors.onBackground, textAlign = TextAlign.Center)
            ) //TODO make constant
        }
    }
}

@Composable
fun EmptyWidgetSounds(modifier: GlanceModifier = GlanceModifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier.padding(10.dp).fillMaxSize().background(GlanceTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.no_sounds_at_widget),
                style = TextStyle(GlanceTheme.colors.onBackground)
            )
            Spacer(modifier = GlanceModifier.height(10.dp))
            Button(
                text = context.getString(R.string.open_app_label),
                onClick = actionStartActivity<MainActivity>()
            )
        }
    }
}

