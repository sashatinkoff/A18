package com.isidroid.logger

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
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
            type = "application/octet-stream"
        }
    }

    fun deviceInfo(): String {
        val os = try {
            Build.VERSION_CODES::class.java.fields[android.os.Build.VERSION.SDK_INT].name
        } catch (e: Exception) {
            ""
        }

        return StringBuilder().apply {
            append("device=${Build.MANUFACTURER} ${Build.BRAND} ${Build.MODEL}\n")
            append("${Build.BRAND} $os ${Build.VERSION.RELEASE}, API ${Build.VERSION.SDK_INT}\n")
            append("Screen ${Resources.getSystem().displayMetrics.widthPixels}x${Resources.getSystem().displayMetrics.heightPixels}\n")
        }.toString()
    }
}