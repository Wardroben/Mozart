package com.example.mozart.presentation.sounds_grid.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.mozart.R

@Composable
fun SoundsBottomAppBarActions(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit,
    onSort: () -> Unit
) {
    /*
    * Open sorting modal sheet
     */
    IconButton(onClick = onSort) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_list_24),
            contentDescription = stringResource(R.string.sort_sounds_content_description)
        )
    }
    /*
    * Activate find actionGroup
     */
    IconButton(onClick = onSearch) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_sounds_content_description)
        )
    }
}