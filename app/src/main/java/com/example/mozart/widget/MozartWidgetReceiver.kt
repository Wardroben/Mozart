package com.example.mozart.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class MozartWidgetReceiver: GlanceAppWidgetReceiver() {
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }
    override val glanceAppWidget: GlanceAppWidget
        get() = MozartWidget()
}