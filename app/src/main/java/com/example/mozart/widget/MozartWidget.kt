package com.example.mozart.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
        //val appContext = context.applicationContext
        val viewModel = EntryPoints.get(context, WidgetEntryPoint::class.java).provideViewModel()
        provideContent {
            GlanceTheme {
                val sounds by viewModel.sounds.collectAsState()
                val controllerManager =
                    remember { MediaControllerManager.getInstance(context.applicationContext) }
                val controller by controllerManager.controller
                val playingSoundId = remember { mutableStateOf<Long?>(null) }

                controller?.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        if (events.contains(Player.EVENT_TRACKS_CHANGED))
                            playingSoundId.value = player.currentMediaItem?.mediaId?.toLong()
                        //viewModel.setPlayingSound(player.currentMediaItem?.mediaId?.toLong())
                        super.onEvents(player, events)
                    }
                })
                WidgetContent(
                    sounds = sounds,
                    playingSoundId = playingSoundId.value,
                    onPlayClick = {
                        if (controller?.isPlaying == false) {
                            //when sounds not playing before
                            controller?.run {
                                setMediaItem(getMediaItem(it))
                                prepare()
                                play()
                            }
                        } else if (controller?.currentMediaItem?.mediaId?.toLong() == it.id) {
                            //stop playing sound
                            controller?.stop()
                            controller?.clearMediaItems()
                        } else {
                            //set new item to play
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