package com.isidroid.logger

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.io.File


class DiagnosticsConfig(
    application: Context,
    appName: String,
    authority: String = "",
    tree: Timber.DebugTree = YDebugTree(),
    disableCrashlytics: Boolean = false
) {

    init {
        val fileAuthority = if (authority.isEmpty()) "$appName.fileprovider" else authority
        val directory = File(application.cacheDir, LOGCAT_BASEDIR)
            .apply { if (!exists()) mkdirs() }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!disableCrashlytics)
        Diagnostics(
            context = application.applicationContext,
            authority = fileAuthority,
            debugTree = tree,
            baseDir = directory
        )

        Timber.plant(tree)
    }
}