package com.example.mozart.presentation.sounds_grid.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mozart.R
import com.example.mozart.domain.model.Sound
import com.example.mozart.presentation.sounds_grid.components.SheetActionGroup.Filter
import com.example.mozart.presentation.sounds_grid.components.SheetActionGroup.SoundControl

@Composable
fun ModalSheetContent(
    modifier: Modifier = Modifier,
    sheetActionsGroup: SheetActionGroup?,
    onEditSound: (Sound) -> Unit,
    onDeleteSound: (Sound) -> Unit,
    addSoundToWidget: (Sound) -> Unit,
    removeSoundFromWidget: (Sound) -> Unit,
    showAllSounds: () -> Unit,
    filterOnWidget: () -> Unit,
    hideSheet: () -> Unit
) {
    when (sheetActionsGroup) {
        is Filter -> {
            SoundFilterSheetActions(filterOnWidget, showAllSounds, hideSheet)
        }

        is SoundControl -> {
            SoundControlSheetActions(
                onDeleteSound,
                sheetActionsGroup.sound,
                onEditSound,
                removeSoundFromWidget,
                addSoundToWidget,
                hideSheet
            )
        }

        null -> emptyList<SheetActionGroup>()
    }
}

@Composable
private fun SoundFilterSheetActions(
    filterOnWidget: () -> Unit,
    showAllSounds: () -> Unit,
    hideSheet: () -> Unit
) {
    BottomSheetItem(
        icon = R.drawable.baseline_widgets_24,
        title = R.string.label_at_widget,
        contentDescription = R.string.label_at_widget,
        onClick = { filterOnWidget(); hideSheet() }
    )
    BottomSheetItem(
        icon = R.drawable.baseline_filter_list_off_24,
        title = R.string.label_all_sounds,
        contentDescription = R.string.label_all_sounds,
        onClick = { showAllSounds(); hideSheet() }
    )
}

@Composable
private fun SoundControlSheetActions(
    onDeleteSound: (Sound) -> Unit,
    sound: Sound,
    onEditSound: (Sound) -> Unit,
    removeSoundFromWidget: (Sound) -> Unit,
    addSoundToWidget: (Sound) -> Unit,
    hideSheet: () -> Unit
) {
    BottomSheetItem(
        icon = R.drawable.baseline_delete_outline_24,
        title = R.string.delete_sound_label,
        contentDescription = R.string.delete_sound_label,
        onClick = { onDeleteSound(sound); hideSheet() }
    )
    BottomSheetItem(
        icon = R.drawable.baseline_edit_document_24,
        title = R.string.edit_sound_label,
        contentDescription = R.string.edit_sound_label,
        onClick = { onEditSound(sound); hideSheet() }
    )
    if (sound.atWidget) BottomSheetItem(
        icon = R.drawable.baseline_bookmark_remove_24,
        title = R.string.remove_from_widget_label,
        contentDescription = R.string.remove_from_widget_label,
        onClick = { removeSoundFromWidget(sound); hideSheet() }
    )
    else BottomSheetItem(
        icon = R.drawable.baseline_bookmark_add_24,
        title = R.string.add_to_widget_label,
        contentDescription = R.string.add_to_widget_label,
        onClick = { addSoundToWidget(sound); hideSheet() }
    )
}


object SheetActions {
    val filterActions = listOf(
        BottomSheet(
            icon = R.drawable.baseline_widgets_24,
            title = R.string.label_at_widget,
            contentDescription = R.string.label_at_widget,
        ),
        BottomSheet(
            icon = R.drawable.baseline_filter_list_off_24,
            title = R.string.label_all_sounds,
            contentDescription = R.string.label_all_sounds,
        )
    )

    val soundControlActions = listOf(
        BottomSheet(
            icon = R.drawable.baseline_delete_outline_24,
            title = R.string.delete_sound_label,
            contentDescription = R.string.delete_sound_label,
        ),
        BottomSheet(
            icon = R.drawable.baseline_edit_document_24,
            title = R.string.edit_sound_label,
            contentDescription = R.string.edit_sound_label,
        ),
        BottomSheet(
            icon = R.drawable.baseline_bookmark_remove_24,
            title = R.string.remove_from_widget_label,
            contentDescription = R.string.remove_from_widget_label,
        ),
        BottomSheet(
            icon = R.drawable.baseline_bookmark_add_24,
            title = R.string.add_to_widget_label,
            contentDescription = R.string.add_to_widget_label,
        )
    )
}

@Immutable
data class BottomSheet(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val contentDescription: Int,
)

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
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.list_item_padding)
        )
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = contentDescription)
        )
        Text(text = stringResource(id = title))
    }
}

sealed class SheetActionGroup {
    data object Filter : SheetActionGroup()
    data class SoundControl(val sound: Sound) : SheetActionGroup()
}
