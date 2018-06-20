package com.isidroid.loggermodule

import timber.log.Timber

open class YDebugTree : Timber.DebugTree() {
    private var fileLoggers = mutableListOf<FileLogger>()

    fun startLogger(logger: FileLogger) {
        fileLoggers.add(logger)
    }

    fun stopLogger(tag: String? = null) {
        var logger = if (tag != null) fileLoggers.firstOrNull { it.tag == tag }
        else fileLoggers.lastOrNull()

        logger?.let { fileLoggers.remove(it) }
    }

    fun stopAll() {
        fileLoggers.clear()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        fileLoggers.forEach { it.log(priority, tag, message, t) }
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