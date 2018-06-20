package com.isidroid.loggermodule

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import io.reactivex.Flowable
import java.io.File

class Diagnostics {
    var authority: String? = null
    private lateinit var baseDir: File
    private var debugTree = YDebugTree()

    fun start(tag: String = DEFAULT_LOG_FILENAME) {
        debugTree.startLogger(FileLogger(baseDir, tag))
    }

    fun stop(tag: String? = null) {
        debugTree.stopLogger(tag)
    }

    fun stopAll() {
        debugTree.stopAll()
    }

    private fun uri(context: Context, file: File): Uri? {
        return if (!authority.isNullOrEmpty()) {
            FileProvider.getUriForFile(context, authority!!, file)
        } else null
    }

    fun getLogs(context: Context, withLogcat: Boolean = false): Flowable<MutableList<LogData>> {
        return Flowable.just(baseDir)
                .map {
                    var result = mutableListOf<LogData>()
                    baseDir.listFiles()
                            ?.filter { it.isFile }
                            ?.forEach { result.add(LogData(it, uri(context, it))) }

                    if (withLogcat) {
                        var file = File(baseDir, "logcat.log").apply { createNewFile() }
                        Runtime.getRuntime().exec("logcat -f" + " ${file.absolutePath}")
                        result.add(LogData(file, uri(context, file)))
                    }
                    result
                }
    }

    fun getShareLogsIntent(context: Context, withLogcat: Boolean = false): Flowable<Intent> {
        return getLogs(context, withLogcat).map { Utils.shareLogsIntent(it) }
    }

    fun clearLogs() {
        baseDir.deleteRecursively()
    }

    companion object {
        lateinit var instance: Diagnostics

        fun create(context: Context): Diagnostics {
            instance = Diagnostics().apply {
                baseDir = File(context.cacheDir, "logs")
            }
            return instance!!
        }
    }

    data class LogData(val file: File, val uri: Uri? = null)
}