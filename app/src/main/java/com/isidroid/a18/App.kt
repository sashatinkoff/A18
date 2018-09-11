package com.isidroid.a18

import android.app.Application
import com.isidroid.logger.DiagnosticsConfig
import com.isidroid.realm.RealmConfig


class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        RealmConfig(this)
                .version(1L)
                .migration(null)
                .create()

        DiagnosticsConfig(this)
                .appname(BuildConfig.APPLICATION_ID)
                .disableCrashlytics(BuildConfig.DEBUG)
                .create()

        BindAdapter.create()
        NotificationsChannels()
    }
}