package com.example.mozart.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.example.mozart.di.WidgetEntryPoint
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.mediasession.MediaControllerManager
import dagger.hilt.EntryPoints

class MozartWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val viewModel = EntryPoints.get(appContext, WidgetEntryPoint::class.java).provideViewModel()
        provideContent {
            GlanceTheme {
                val uiState by viewModel.uiState.collectAsState()
                val controllerManager = remember { MediaControllerManager.getInstance(context) }
                val controller by controllerManager.controller

                controller?.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        if (events.contains(Player.EVENT_TRACKS_CHANGED)) viewModel.setPlayingSound(
                            player.currentMediaItem?.mediaId?.toLong()
                        )
                    }
                })
                WidgetContent(
                    sounds = uiState.sounds,
                    playingSoundId = uiState.playingSoundId,
                    onPlayClick = {
                        if (controller?.isPlaying == false) {
                            //when sounds not playing before
                            controller?.run {
                                //viewModel.setPlayingSound(it.id)
                                setMediaItem(getMediaItem(it))
                                prepare()
                                play()
                            }
                        } else if (controller?.currentMediaItem?.mediaId?.toLong() == it.id) {
                            //stop playing sound
                            //viewModel.setPlayingSound(it.id)
                            controller?.stop()
                            controller?.clearMediaItems()
                        } else {
                            //set new item to play
                            //viewModel.setPlayingSound(it)
                            controller?.setMediaItem(getMediaItem(it))
                            controller?.play()
                        }
                    })
            }
        }
    }

    private fun getMediaItem(sound: Sound): MediaItem {
        return MediaItem.Builder()
            .setMediaId(sound.id.toString())
            .setUri(sound.uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(sound.fileName.substringBeforeLast('.'))
                    .build()
            )
            .build()
    }
}