package com.isidroid.logger

import java.io.File

class FileLogger(private val baseDir: File, internal val name: String = "diagnostics") {
    var file: File? = null
    private var filter = ""

    fun filter(value: String) = apply { this.filter = value }
    fun create() = apply {
        val file = File(baseDir, "$name.log")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists() || file.length() >= 1048576) file.createNewFile()

        this.file = file

        save(
            "Debug $filter, created at ${Utils.now()}\n" +
                    "${Utils.deviceInfo()}\n" +
                    "============================" +
                    "\n", true
        )
    }

    private fun isFilterApplied(message: String) =
        (filter.isNotEmpty() && message.toLowerCase().contains(filter)) || filter.isEmpty()

    fun log(priority: Int, tag: String?, iMessage: String, t: Throwable?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $tag $iMessage $t"
        if (isFilterApplied(message)) save(message)
    }

    fun log(priority: Int, iMessage: String?, vararg args: Any?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $iMessage $args"
        if (isFilterApplied(message)) save(message)
    }

    fun log(priority: Int, t: Throwable?, iMessage: String?, vararg args: Any?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $iMessage $args $t"
        if (isFilterApplied(message)) save(message)
    }

    fun log(priority: Int, t: Throwable?) {
        val message = "${Utils.now()} ${Utils.prefixForPriority(priority)} $t"
        if (isFilterApplied(message)) save(message)
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