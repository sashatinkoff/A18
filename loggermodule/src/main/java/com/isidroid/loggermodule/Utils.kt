package com.isidroid.loggermodule

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


internal object Utils {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun prefixForPriority(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "[VERBOSE]"
            Log.DEBUG -> "[DEBUG]"
            Log.INFO -> "[INFO]"
            Log.WARN -> "[WARN]"
            Log.ERROR -> "[ERROR]"
            Log.ASSERT -> "[ASSERT]"
            else -> "[UNKNOWN($priority)]"
        }
    }

    fun now(dateFormat: SimpleDateFormat = this.dateFormat): String {
        return dateFormat.format(Date())
    }
}