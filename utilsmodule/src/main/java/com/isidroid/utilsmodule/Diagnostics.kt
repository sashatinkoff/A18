package com.isidroid.utilsmodule

import android.util.Log.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val CH_SPACE = " "

open class Diagnostics(private val file: File, private var saveLogs: Boolean = true) : Timber.DebugTree() {
    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    init {
        create()
    }

    private fun create() {
        if (!saveLogs) return

        if (file.exists()) file.delete()
        else {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        file.printWriter().use { it.print("Created at ${now(SimpleDateFormat("yyyy-MM-dd HH:mm:ss "))}\n\n") }
    }

    fun saveLogs(saveLogs: Boolean = true) {
        this.saveLogs = saveLogs
        if (saveLogs) create()
    }

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        var message = (message ?: "") + argsToString(args)
        save(message, priority)
    }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        var message = (message ?: "") + argsToString(args)
        save(message, priority)
    }

    override fun log(priority: Int, t: Throwable?) {
        save("", priority)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        save(message, priority, tag)
    }

    private fun argsToString(vararg args: Any?): String {
        return " $args"
    }

    private fun priority(priority: Int): String {
        return when (priority) {
            VERBOSE -> "V"
            DEBUG -> "D"
            INFO -> "I"
            WARN -> "W"
            ERROR -> "E"
            ASSERT -> "A"
            else -> ""
        }
    }

    private fun now(dateFormat: SimpleDateFormat? = null): String {
        var dateFormat = dateFormat ?: this.dateFormat
        return dateFormat.format(Date())
    }

    private fun save(message: String, priority: Int, tag: String? = null) {
        if (!saveLogs) return
        var builder = StringBuilder().apply {
            append("\n")
            append(now()).append(CH_SPACE)
            append(priority(priority)).append(CH_SPACE)
            tag?.let { append("$tag").append(CH_SPACE) }
            append(message).append(CH_SPACE)
        }

        file.appendText(builder.toString())
    }

    fun logcat(): File? {
        return try {
            val file = File(file.parent, "logcat.log")
            var process = Runtime.getRuntime().exec("logcat -d")
            process = Runtime.getRuntime().exec("logcat -f $file")
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}