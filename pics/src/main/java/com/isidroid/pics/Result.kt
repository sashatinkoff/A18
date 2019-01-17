package com.isidroid.pics

import android.webkit.MimeTypeMap
import androidx.core.content.MimeTypeFilter
import timber.log.Timber
import java.io.Serializable
import java.util.*

class Result : Serializable {
    var dateTaken: Date? = null
    var localPath: String? = null
    var orientation: Int = 0
    fun isImage() = isType("jpg", "png", "jpeg")
    fun isType(vararg exts: String) = exts.toList().contains(MimeTypeMap.getFileExtensionFromUrl(localPath).toLowerCase())

    override fun toString(): String {
        return "Result{" +
                ", dateTaken=" + dateTaken +
                ", localPath='" + localPath + '\''.toString() +
                ", orientation=" + orientation +
                '}'.toString()
    }
}