package com.example.mozart.presentation.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.example.mozart.di.WidgetEntryPoint
import com.example.mozart.domain.repository.SoundRepository
import dagger.hilt.EntryPoints
import dagger.hilt.android.EntryPointAccessors

class MozartWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val viewModel = EntryPoints.get(appContext, WidgetEntryPoint::class.java).provideViewModel()
        provideContent {
            GlanceTheme {
                val uiState by viewModel.uiState.collectAsState()
                WidgetContent(sounds = uiState.sounds)
            }
        }
    }
}

private fun getSoundRepository(context: Context): SoundRepository {
    val hiltEntryPoint = EntryPointAccessors.fromApplication(context, WidgetEntryPoint::class.java)
    return hiltEntryPoint.provideSoundRepository()
}