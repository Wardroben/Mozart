package com.example.mozart.presentation.sounds_grid.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mozart.R

@Composable
fun SoundIcon(modifier: Modifier = Modifier, isPlaying: Boolean) {
    val playIconId = R.drawable.baseline_play_circle_24
    val stopIconId = R.drawable.baseline_stop_circle_24
    Icon(
        modifier = modifier,
        painter = painterResource(id = if (isPlaying) stopIconId else playIconId),
        contentDescription = null //TODO content description for play button
    )
}

@Preview
@Composable
private fun SoundButtonPreview() {
    SoundIcon(isPlaying = true)
}