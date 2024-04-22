package com.example.mozart.widget

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mozart.di.WidgetEntryPoint
import com.example.mozart.domain.model.sound.Sound
import com.example.mozart.mediasession.PlaybackService
import dagger.hilt.EntryPoints

class MozartWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        //val appContext = context.applicationContext
        val viewModel = EntryPoints.get(context, WidgetEntryPoint::class.java).provideViewModel()
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        var controller by mutableStateOf<MediaController?>(null)
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            { controller = controllerFuture.get() },
            ContextCompat.getMainExecutor(context)
        )
        provideContent {
            GlanceTheme {
                var playingSoundId by remember { mutableStateOf<Long?>(null) }
                val uiState by viewModel.sounds.collectAsState()

                controller?.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        if (events.contains(Player.EVENT_TRACKS_CHANGED)) {
                            playingSoundId = player.currentMediaItem?.mediaId?.toLong()
                        }
                        super.onEvents(player, events)
                    }
                })

                WidgetContent(
                    sounds = uiState.sounds,
                    errorMessage = uiState.errorMessage,
                    isLoading = uiState.isLoading,
                    playingSoundId = playingSoundId,
                    onPlayClick = {
                        if (controller?.isPlaying == false) {
                            //when sounds not playing before
                            controller?.run {
                                setMediaItem(getMediaItem(it))
                                prepare()
                                play()
                            }
                        } else if (controller?.currentMediaItem?.mediaId?.toLong() == it.id) {
                            //stop playing sounds
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