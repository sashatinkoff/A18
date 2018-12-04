package com.isidroid.a18.core

import android.app.Application
import com.bumptech.glide.Glide
import com.isidroid.a18.BuildConfig
import com.isidroid.logger.DiagnosticsConfig
import com.isidroid.realm.RealmConfig
import com.isidroid.utils.DataBindingConfig
import com.isidroid.utils.utils.ScreenUtils
import com.isidroid.utils.utils.UpgradeHelper

object AppInit {
    fun create(app: Application) {
        DiagnosticsConfig(app)
                .appname(BuildConfig.APPLICATION_ID)
                .disableCrashlytics(BuildConfig.DEBUG)
                .create()

        RealmConfig(app)
                .version(1L)
                .migration(null)
                .create()

        ScreenUtils.create(app)
        UpgradeHelper.create(app, BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME)

        DataBindingConfig.create()
                .withImageLoader { imageView, url -> Glide.with(imageView).load(url).into(imageView) }

        NotificationsChannels()
    }
}