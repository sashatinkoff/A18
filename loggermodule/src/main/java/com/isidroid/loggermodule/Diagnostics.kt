package com.isidroid.loggermodule

import android.content.Context
import timber.log.Timber

class Diagnostics private constructor(context: Context) {
    private var baseDir = context.cacheDir
    private var debugTree = YDebugTree()

    fun start(tag: String = "events") {
        debugTree.startLogger(FileLogger(baseDir, tag))
    }

    fun stop(tag: String? = null) {
        debugTree.stopLogger(tag)
    }

    fun stopAll() {
        debugTree.stopAll()
    }

    companion object {
        private var isInit = false
        lateinit var get: Diagnostics

        fun create(context: Context) {
            if (!isInit) {
                get = Diagnostics(context)
                Timber.plant(Timber.DebugTree())
            }
            isInit = true
        }
    }
}