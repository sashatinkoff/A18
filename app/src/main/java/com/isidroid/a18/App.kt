package com.isidroid.a18

import android.app.Application
import com.isidroid.logger.Diagnostics
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


        Diagnostics.create(this).apply {
            authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
        }

        BindAdapter.create()
        NotificationsChannels()
    }
}