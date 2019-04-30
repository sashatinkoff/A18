package com.isidroid.logger

import java.io.File

class FileLogger(private val baseDir: File = Diagnostics.instance.baseDir,
                 val name: String = "diagnostics") {

    var file: File? = null
    private var isRecreate = false
    private var maxFileSize = 500f * 1024f
    private var filters = mutableListOf<String>()

    fun recreate(value: Boolean) = apply { isRecreate = value }
    fun maxFileSizeMb(value: Float) = apply { this.maxFileSize = value * 1024 * 1024 }
    fun filter(vararg value: String) = apply { filters.addAll(value) }
    fun content() = try {
        file?.readText()
    } catch (e: Exception) {
        null
    }

    internal fun create() = apply {
        val file = File(baseDir, "$name.log")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        this.file = file

        if (!file.exists() || file.length() >= (maxFileSize) || isRecreate) {
            isRecreate = false
            file.createNewFile()
            save(
                    "Debug for $filters, created at ${Utils.now()}\n" +
                            Utils.deviceInfo() +
                            "---" +
                            "\n", true
            )

        } else {
            save("\n===Start new session at ${Utils.now()}===\n")
        }

    }

    private fun isFilterApplied(message: String) = filters.any { message.toLowerCase().contains(it) }
    internal fun log(priority: Int, tag: String?, message: String?, t: Throwable?, vararg args: Any?) {
        val builder = StringBuilder(Utils.now())
                .append(" ")
                .append(Utils.prefixForPriority(priority))
                .append(" ")

        tag?.let { builder.append(it).append(" ") }
        t?.let { builder.append(it.message).append(" ") }
        message?.let { builder.append(it).append(" ") }

        if (args.isNotEmpty() && args.any { it != null }) {
            builder.append("[")
            args.filter { it != null }.forEach { builder.append("$it, ") }
            builder.substring(0, builder.length - 1)
            builder.append("]")
        }

        val data = builder.toString()
        if (isFilterApplied(data)) save(data)
    }

    private fun save(message: String, isCreate: Boolean = false) {
        try {
            if (isCreate) file?.writeText("$message\n")
            else file?.appendText("$message\n")
        } catch (e: Exception) {
        }
    }

    fun destroy() {
        file?.delete()
    }

}