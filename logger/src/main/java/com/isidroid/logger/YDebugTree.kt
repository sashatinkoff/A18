package com.isidroid.logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

open class YDebugTree : Timber.DebugTree() {
    enum class Key {
        PRIORITY, TAG, MESSAGE
    }

    internal var fileLoggers = mutableListOf<FileLogger>()

    fun startLogger(logger: FileLogger) {
        fileLoggers.add(logger)
    }

    fun stopLogger(tag: String? = null) {
        val logger = if (tag != null) fileLoggers.firstOrNull { it.name == tag }
        else fileLoggers.lastOrNull()

        logger?.let { fileLoggers.remove(it) }
    }

    fun stopAll() {
        fileLoggers.clear()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        fileLoggers.forEach { it.log(priority, tag, message, t, null) }

        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
            return

        val exception = t?.let { Exception(it) } ?: Exception(message)
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey(Key.PRIORITY.name, priority)
            setCustomKey(Key.MESSAGE.name, message)
            tag?.let { setCustomKey(Key.TAG.name, tag) }
            recordException(exception)
        }
    }

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        super.log(priority, message, *args)
        fileLoggers.forEach { it.log(priority, null, message, null, args) }
    }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        super.log(priority, t, message, *args)
        fileLoggers.forEach { it.log(priority, null, message, t, args) }
    }

    override fun log(priority: Int, t: Throwable?) {
        super.log(priority, t)
        fileLoggers.forEach { it.log(priority, null, null, t, null) }
    }
}