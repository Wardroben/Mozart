package com.example.mozart.presentation.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
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
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.presentation.MainActivity

@Composable
fun WidgetContent(
    modifier: GlanceModifier = GlanceModifier,
    sounds: List<Sound>,
) {
    if (sounds.isEmpty()) EmptyWidgetSounds()
    else WidgetSoundGrid(
        sounds = sounds,
        modifier = modifier.fillMaxSize().padding(10.dp)
    )
}

@Composable
fun WidgetSoundGrid(modifier: GlanceModifier = GlanceModifier, sounds: List<Sound>) {
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
                modifier = GlanceModifier.padding(15.dp)
            )
        }
    }
}

@Composable
fun WidgetSoundItem(
    modifier: GlanceModifier = GlanceModifier,
    title: String
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(resId = R.drawable.baseline_play_circle_24),
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

