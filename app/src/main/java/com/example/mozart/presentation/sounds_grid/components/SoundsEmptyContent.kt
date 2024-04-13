package com.example.mozart.presentation.sounds_grid.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mozart.R

@Composable
fun SoundsEmptyContent(
    @StringRes noSoundsLabel: Int,
    @DrawableRes noSoundsIcon: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noSoundsIcon),
            contentDescription = stringResource(R.string.no_sounds_image_content_description),
            modifier = Modifier.size(64.dp)
        )
        Text(stringResource(id = noSoundsLabel))
    }
}