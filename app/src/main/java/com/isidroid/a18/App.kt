package com.isidroid.a18

import android.app.Application
import com.bumptech.glide.Glide
import com.isidroid.a18.core.NotificationsChannels
import com.isidroid.logger.DiagnosticsConfig
import com.isidroid.realm.RealmConfig
import com.isidroid.utils.DataBindingConfig
import com.isidroid.utils.utils.ScreenUtils
import com.isidroid.utils.utils.UpgradeHelper


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

        ScreenUtils.create(this)
        UpgradeHelper.create(this, BuildConfig.VERSION_CODE)

        DataBindingConfig.create()
                .withImageLoader { imageView, url -> Glide.with(imageView).load(url).into(imageView) }

        NotificationsChannels()
    }
}