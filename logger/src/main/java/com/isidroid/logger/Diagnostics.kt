package com.isidroid.logger

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import io.reactivex.Flowable
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

const val LOGCAT_FILENAME = "logcat.log"
const val LOGCAT_BASEDIR = "diagnostics"

class Diagnostics {
    var authority: String? = null
    lateinit var baseDir: File
    var debugTree: Timber.DebugTree = YDebugTree()
    var logsStart: Date? = null

    fun start() {
        logsStart = Date()
    }

    fun cancel() {
        logsStart = null
    }

    fun createLogger(logger: FileLogger) = apply { (debugTree as? YDebugTree)?.startLogger(logger.create()) }
    fun createLogger(filename: String, filter: String = "") = apply {
        (debugTree as? YDebugTree)?.startLogger(FileLogger(baseDir, filename).filter(filter).create())
    }

    fun destroyLogger(filename: String) = apply { (debugTree as? YDebugTree)?.stopLogger(filename) }
    fun stopAll() = apply { (debugTree as? YDebugTree)?.stopAll() }

    fun destroyAllLoggers() = apply {
        (debugTree as? YDebugTree)?.fileLoggers?.forEach { it.destroy() }
        (debugTree as? YDebugTree)?.stopAll()
    }

    private fun uri(context: Context, file: File): Uri? {
        return if (!authority.isNullOrEmpty()) {
            FileProvider.getUriForFile(context, authority!!, file)
        } else null
    }

    fun getLogs(context: Context, withLogcat: Boolean = false): Flowable<MutableList<LogData>> {
        val result = mutableListOf<LogData>()

        val loggers = (debugTree as? YDebugTree)?.fileLoggers ?: mutableListOf()
        loggers.filter { it.file?.exists() == true }
            .forEach {
                val file = it.file!!
                result.add(LogData(file, uri(context, file)))
            }

        return Flowable.just(result)
            .doOnNext { collectStandardLogs(context, withLogcat, it) }
    }


    private fun collectStandardLogs(context: Context, withLogcat: Boolean, result: MutableList<LogData>) {
        if (withLogcat || logsStart != null) {
            val command = "logcat -d"
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

            val process = Runtime.getRuntime().exec(command)
            val bufferedReader = BufferedReader(
                InputStreamReader(process.inputStream)
            )

            // Grab the results
            val log = StringBuilder().append(Utils.deviceInfo())
            var line: String?

            do {
                line = bufferedReader.readLine()

                val isLog = if (logsStart != null) {
                    val time = try {
                        dateFormat.parse("$year-$line")
                    } catch (e: Exception) {
                        Date()
                    }
                    time.after(logsStart)
                } else true

                if (isLog && line != null) log.append(line + "\n")
            } while (line != null)


            // save in file
            if (log.isNotEmpty()) {
                val file = File(baseDir, LOGCAT_FILENAME).apply {
                    createNewFile()
                    writeText(log.toString())
                }
                result.add(LogData(file, uri(context, file)))
            }
        }
    }

    fun getShareLogsIntent(context: Context, withLogcat: Boolean = false): Flowable<Intent> {
        return getLogs(context, withLogcat)
            .map { Utils.shareLogsIntent(it) }
            .doOnNext { cancel() }
    }

    fun clearLogs() {
        baseDir.deleteRecursively()
        baseDir.mkdirs()
    }

    data class LogData(val file: File, val uri: Uri? = null)

    companion object {
        const val LOGTAG = "Diagnostics"
        val instance = Diagnostics()
    }
}