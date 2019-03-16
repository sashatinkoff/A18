package com.isidroid.logger

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

private const val CRASHLYTICS_KEY_PRIORITY = "priority"
private const val CRASHLYTICS_KEY_TAG = "tag"
private const val CRASHLYTICS_KEY_MESSAGE = "message"

open class YDebugTree : Timber.DebugTree() {
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
        fileLoggers.forEach { it.log(priority, tag, message, t) }

        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
            return

        Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority)
        Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag)
        Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message)

        val exception = t?.let { Exception(it) } ?: Exception(message)
        Crashlytics.logException(exception)
    }

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        super.log(priority, message, *args)
        fileLoggers.forEach { it.log(priority, message, *args) }
    }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        super.log(priority, t, message, *args)
        fileLoggers.forEach { it.log(priority, t, message, *args) }
    }

    override fun log(priority: Int, t: Throwable?) {
        super.log(priority, t)
        fileLoggers.forEach { it.log(priority, t) }
    }


}