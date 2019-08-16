package com.isidroid.pics

import android.webkit.MimeTypeMap
import java.io.Serializable
import java.util.*

class ImageInfo : Serializable {
    var dateTaken: Date? = null
    var localPath: String? = null
    var orientation: Int = 0
    fun isImage() = isType("jpg", "png", "jpeg")
    fun isType(vararg exts: String) = exts.toList().contains(MimeTypeMap.getFileExtensionFromUrl(localPath).toLowerCase())

    override fun toString(): String {
        return "ImageInfo{" +
                ", dateTaken=" + dateTaken +
                ", localPath='" + localPath + '\''.toString() +
                ", orientation=" + orientation +
                '}'.toString()
    }
}