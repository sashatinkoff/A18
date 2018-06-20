package com.isidroid.loggermodule

import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileLogger(baseDir: File, val tag: String) {
    private var file: File? = null

    init {
        val sf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        var limit = if (tag.length > 10) 10 else tag.length
        var tagname = tag
                .replace("[^a-z A-Z0-9 .]+", "")
                .replace(" ", "_").substring(0, limit)
                .toLowerCase()

        var filename = StringBuilder(sf.format(Date())).apply {
            append("_")
            append(tagname)
            append(".log")
        }.toString()

        var file = File(baseDir, filename)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        file.createNewFile()

        save("Debug $tag, created at ${Utils.now()}")

        Timber.i("created file = ${file.absolutePath}")

        this.file = file
    }

    fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $tag $message $t"
        save(message)
    }

    fun log(priority: Int, message: String?, vararg args: Any?) {
        var message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $message $args"
        save(message)
    }

    fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        var message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $message $args $t"
        save(message)
    }

    fun log(priority: Int, t: Throwable?) {
        var message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $t"
        save(message)
    }

    private fun save(message: String) {
        try {
            file?.printWriter()?.write("$message\n")
        } catch (e: Exception) {

        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileLogger

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }


}