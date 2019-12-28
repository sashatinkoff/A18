package com.isidroid.a18.core

import android.app.Application
import com.isidroid.a18.BuildConfig
import com.isidroid.logger.DiagnosticsConfig

object AppInit {
    fun create(app: Application) {
        DiagnosticsConfig(
            application = app,
            appName = BuildConfig.APPLICATION_ID,
            disableCrashlytics = BuildConfig.DEBUG
        )

        NotificationsChannels()
    }
}