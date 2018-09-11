package com.isidroid.logger

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_LOG_FILENAME = "events"

class FileLogger(baseDir: File, val tag: String) {
    private var file: File? = null

    init {
        val sf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val limit = if (tag.length > 10) 10 else tag.length
        val tagname = tag
                .replace("[^a-z A-Z0-9 .]+", "")
                .replace(" ", "_").substring(0, limit)
                .toLowerCase()

        val filename = StringBuilder().apply {
            if (DEFAULT_LOG_FILENAME != tag) {
                append(sf.format(Date()))
                append("_")
            }
            append(tagname)
            append(".log")
        }.toString()

        val file = File(baseDir, filename)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        file.createNewFile()
        this.file = file

        save("Debug $tag, created at ${Utils.now()}", true)
    }

    fun log(priority: Int, tag: String?, iMessage: String, t: Throwable?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $tag $iMessage $t"
        save(message)
    }

    fun log(priority: Int, iMessage: String?, vararg args: Any?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $iMessage $args"
        save(message)
    }

    fun log(priority: Int, t: Throwable?, iMessage: String?, vararg args: Any?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $iMessage $args $t"
        save(message)
    }

    fun log(priority: Int, t: Throwable?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $t"
        save(message)
    }

    private fun save(message: String, isCreate: Boolean = false) {
        try {
            if (isCreate) file?.writeText("$message\n")
            else file?.appendText("$message\n")
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