package com.isidroid.logger

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import java.io.File


class DiagnosticsConfig(private val context: Context) {
    private var authority: String? = null
    private var tree: Timber.DebugTree = YDebugTree()
    private var disableCrashlytics = false

    fun appname(name: String) = apply { this.authority = "$name.fileprovider" }
    fun authority(authority: String) = apply { this.authority = authority }
    fun tree(tree: Timber.DebugTree) = apply { this.tree = tree }
    fun disableCrashlytics(enabled: Boolean) = apply { this.disableCrashlytics = enabled }

    fun create() {
        Diagnostics.instance.apply {
            authority = this@DiagnosticsConfig.authority
            debugTree = this@DiagnosticsConfig.tree
            baseDir = File(context.cacheDir, LOGCAT_BASEDIR)

            val crashlytics = Crashlytics.Builder()
                    .core(CrashlyticsCore.Builder()
                            .disabled(disableCrashlytics)
                            .build())
                    .build()

            Fabric.with(context, crashlytics)
            Timber.plant(debugTree)
            clearLogs()
        }
    }
}