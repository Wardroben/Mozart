package com.example.mozart.presentation.sounds_grid.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.presentation.sounds_grid.ControlSound
import com.example.mozart.presentation.sounds_grid.FilterSound
import com.example.mozart.presentation.sounds_grid.SoundFilterType
import com.example.mozart.presentation.sounds_grid.components.SheetActionGroup.*

val horizontalMargin = 20.dp
val verticalMargin = 15.dp

@Composable
fun ModalSheetContent(
    modifier: Modifier = Modifier,
    actionGroup: SheetActionGroup,
    onEditSound: (Sound) -> Unit,
    onDeleteSound: (Sound) -> Unit,
    addSoundToWidget: (Sound) -> Unit,
    removeSoundFromWidget: (Sound) -> Unit,
    filterSounds: (SoundFilterType) -> Unit,
    hideSheet: () -> Unit
) {
    if (actionGroup is SoundControl) {
        val sound = actionGroup.sound
        Column(
            modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalMargin, vertical = verticalMargin),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = sound.fileName, style = MaterialTheme.typography.headlineSmall)
        }
    }
    actionGroup.actions.forEach { sheetAction ->
        BottomSheetItem(
            icon = sheetAction.iconRes,
            title = sheetAction.label,
            contentDescription = sheetAction.contentDescription,
            modifier = modifier
        ) {
            when (sheetAction.type) {
                is ControlSound.Delete -> {
                    onDeleteSound(sheetAction.type.sound)
                    hideSheet()
                }

                is ControlSound.Edit -> {
                    onEditSound(sheetAction.type.sound)
                    hideSheet()
                }

                is ControlSound.AddOnWidget -> {
                    addSoundToWidget(sheetAction.type.sound)
                    hideSheet()
                }

                is ControlSound.RemoveFromWidget -> {
                    removeSoundFromWidget(sheetAction.type.sound)
                    hideSheet()
                }

                FilterSound.AllSounds -> {
                    filterSounds(SoundFilterType.ALL_SOUNDS)
                    hideSheet()
                }

                FilterSound.OnWidget -> {
                    filterSounds(SoundFilterType.ON_WIDGET_SOUNDS)
                    hideSheet()
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun BottomSheetItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes contentDescription: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = horizontalMargin,
                vertical = verticalMargin
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.list_item_padding)
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = contentDescription)
        )
        Text(text = stringResource(id = title))
    }
}