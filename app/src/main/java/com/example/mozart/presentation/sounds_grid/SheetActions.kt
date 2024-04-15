package com.example.mozart.presentation.sounds_grid

import com.example.mozart.R
import com.example.mozart.domain.model.sound.Sound

object SheetActions {
    val filterActions = listOf(
        ModalSheetAction(
            iconRes = R.drawable.baseline_widgets_24,
            label = R.string.label_at_widget,
            contentDescription = R.string.label_at_widget,
            type = FilterSound.OnWidget
        ),
        ModalSheetAction(
            iconRes = R.drawable.baseline_filter_list_off_24,
            label = R.string.label_all_sounds,
            contentDescription = R.string.label_all_sounds,
            type = FilterSound.AllSounds
        )
    )

    fun soundControlActions(sound: Sound) = listOf(
        ModalSheetAction(
            iconRes = R.drawable.baseline_delete_outline_24,
            label = R.string.delete_sound_label,
            contentDescription = R.string.delete_sound_label,
            type = ControlSound.Delete(sound)
        ),
        ModalSheetAction(
            iconRes = R.drawable.baseline_edit_document_24,
            label = R.string.edit_sound_label,
            contentDescription = R.string.edit_sound_label,
            type = ControlSound.Edit(sound)
        ),
        if (sound.atWidget) {
            ModalSheetAction(
                iconRes = R.drawable.baseline_bookmark_remove_24,
                label = R.string.remove_from_widget_label,
                contentDescription = R.string.remove_from_widget_label,
                type = ControlSound.RemoveFromWidget(sound)
            )
        } else {
            ModalSheetAction(
                iconRes = R.drawable.baseline_bookmark_add_24,
                label = R.string.add_to_widget_label,
                contentDescription = R.string.add_to_widget_label,
                type = ControlSound.AddOnWidget(sound)
            )
        }
    )
}