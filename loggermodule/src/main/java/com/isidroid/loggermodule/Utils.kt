package com.isidroid.loggermodule

import android.content.Intent
import android.net.Uri
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

    fun shareLogsIntent(data: MutableList<Diagnostics.LogData>): Intent {
        val mutableUris = mutableListOf<Uri>()
        data
                .filter { it.uri != null }
                .forEach { mutableUris.add(it.uri!!) }

        return Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(mutableUris))
            type = "text/*"
        }
    }
}