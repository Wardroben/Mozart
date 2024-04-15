package com.example.mozart.presentation.sounds_grid.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.mozart.common.SoundExamples
import com.example.mozart.domain.model.sound.Sound

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundItem(
    modifier: Modifier = Modifier,
    sound: Sound,
    isPlaying: Boolean,
    onLongClicked: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClicked
            ),
        shape = CardDefaults.elevatedShape
    ) {
        Column(Modifier.padding(10.dp)) {
            SoundIcon(
                isPlaying = isPlaying,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = sound.fileName,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                lineHeight = TextUnit(1.2F, TextUnitType.Em)
            )
        }
    }
}

@Preview(widthDp = 80, heightDp = 80)
@Composable
private fun SoundItemPreview() {
    SoundItem(sound = SoundExamples.sounds[3], isPlaying = false, onLongClicked = {}, onClick = {})
}