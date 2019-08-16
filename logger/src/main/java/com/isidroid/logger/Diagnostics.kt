package com.isidroid.logger

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
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

    fun start() = apply { logsStart = Date() }
    fun cancel() = apply { logsStart = null }

    fun createLogger(logger: FileLogger) = apply { (debugTree as? YDebugTree)?.startLogger(logger.create()) }
    fun createLogger(filename: String, vararg filters: String) = apply {
        (debugTree as? YDebugTree)?.startLogger(FileLogger(baseDir, filename).filter(*filters).create())
    }

    fun destroyLogger(filename: String) = apply { (debugTree as? YDebugTree)?.stopLogger(filename) }
    fun destroyLogger(logger: FileLogger, deleteFile: Boolean = false) = apply {
        (debugTree as? YDebugTree)?.stopLogger(logger.name)
        if (deleteFile) logger.file?.delete()
    }

    fun stopAll() = apply { (debugTree as? YDebugTree)?.stopAll() }
    fun destroyAllLoggers() = apply {
        (debugTree as? YDebugTree)?.fileLoggers?.forEach { it.destroy() }
        (debugTree as? YDebugTree)?.stopAll()
    }

    private fun uri(context: Context, file: File?) = try {
        FileProvider.getUriForFile(context, authority!!, file!!)
    } catch (e: Exception) {
        null
    }

    fun getLogs(context: Context, withLogcat: Boolean = false): MutableList<LogData> {
        val loggers = (debugTree as? YDebugTree)?.fileLoggers ?: mutableListOf()
        val result = loggers
            .filter { it.file?.exists() == true && uri(context, it.file) != null }
            .mapTo(mutableListOf()) { LogData(it.file!!, uri(context, it.file!!)) }

        if (withLogcat) collectStandardLogs(context, result)
        return result
    }

    fun logIntent(context: Context, withLogcat: Boolean = false): Intent {
        val logs = getLogs(context, withLogcat)
        cancel()
        return Utils.shareLogsIntent(logs)
    }

    private fun collectStandardLogs(context: Context, result: MutableList<LogData>) {
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

    data class LogData(val file: File, val uri: Uri? = null)
    companion object {
        val instance by lazy { Diagnostics() }
    }
}