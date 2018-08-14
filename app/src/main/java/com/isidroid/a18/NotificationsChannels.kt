package com.isidroid.a18

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi


class NotificationsChannels {
    private val DEFAULT_SOUND = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) create(App.instance)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun create(app: App) {
        val manager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(defaultChannel())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun defaultChannel(): NotificationChannel {
        val name = "Default channel"
        val description = "Couple of words about purposes of this channel"

        return NotificationChannel(DEFAULT_CHANNEL, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
            this.description = description
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
            setShowBadge(true)
            setSound(DEFAULT_SOUND, null)
            vibrationPattern = longArrayOf(100L, 200L, 300L, 400L, 500L, 400L, 300L, 200L, 400L)
        }
    }

    companion object {
        const val DEFAULT_CHANNEL = "Default channel"
    }

}