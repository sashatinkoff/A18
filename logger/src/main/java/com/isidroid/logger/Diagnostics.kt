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
    internal lateinit var baseDir: File
    internal var debugTree: Timber.DebugTree = YDebugTree()
    private var logsStart: Date? = null

    fun start() {
        logsStart = Date()
    }

    fun cancel() {
        logsStart = null
    }

    fun startInfo(tag: String = DEFAULT_LOG_FILENAME) {
        (debugTree as? YDebugTree)?.startLogger(FileLogger(baseDir, tag))
    }

    fun stopInfo(tag: String? = null) {
        (debugTree as? YDebugTree)?.stopLogger(tag)
    }

    fun stopAll() {
        (debugTree as? YDebugTree)?.stopAll()
    }

    private fun uri(context: Context, file: File): Uri? {
        return if (!authority.isNullOrEmpty()) {
            FileProvider.getUriForFile(context, authority!!, file)
        } else null
    }

    fun getLogs(context: Context, withLogcat: Boolean = false): Flowable<MutableList<LogData>> {
        return Flowable.just(baseDir)
                .map {
                    val result = mutableListOf<LogData>()
                    baseDir.listFiles()
                            ?.filter { file -> file.isFile && file.name != LOGCAT_FILENAME }
                            ?.forEach { file -> result.add(LogData(file, uri(context, it))) }
                    result
                }
                .doOnNext {
                    if (withLogcat || logsStart != null) {
                        val command = "logcat -d"
                        val year = Calendar.getInstance().get(Calendar.YEAR)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

                        val process = Runtime.getRuntime().exec(command)
                        val bufferedReader = BufferedReader(
                                InputStreamReader(process.inputStream))

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
                            it.add(LogData(file, uri(context, file)))
                        }
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