package com.isidroid.a18.core

import android.app.Application
import com.isidroid.a18.BuildConfig
import com.isidroid.logger.DiagnosticsConfig
import com.isidroid.realm.RealmConfig
import com.isidroid.utils.utils.UpgradeHelper

object AppInit {
    fun create(app: Application) {
        DiagnosticsConfig(
            application = app,
            appName = BuildConfig.APPLICATION_ID,
            disableCrashlytics = false
        )

        RealmConfig(
            application = app,
            version = 1L,
            migration = null,
            dbname = "default.realm"
        )

        UpgradeHelper.create(app, BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME)
        NotificationsChannels()
    }
}