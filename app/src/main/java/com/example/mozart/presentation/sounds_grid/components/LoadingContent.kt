package com.example.mozart.presentation.sounds_grid.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    loading: Boolean,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        content()
    }
}