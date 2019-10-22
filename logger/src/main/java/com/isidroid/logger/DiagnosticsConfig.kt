package com.isidroid.logger

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
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

        val crashlyticsConfigBuilder = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(disableCrashlytics).build())

        Diagnostics(
            context = application.applicationContext,
            authority = fileAuthority,
            debugTree = tree,
            baseDir = directory
        )

        Fabric.with(application, crashlyticsConfigBuilder.build())
        Timber.plant(tree)
    }
}