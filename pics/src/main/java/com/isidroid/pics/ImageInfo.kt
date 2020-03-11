package com.isidroid.pics

import android.webkit.MimeTypeMap
import java.io.Serializable
import java.util.*

data class ImageInfo(
    var dateTaken: Date? = null,
    var localPath: String? = null,
    var orientation: Int = 0
) : Serializable {

    fun isImage() = isType("jpg", "png", "jpeg")
    fun isType(vararg exts: String) =
        exts.toList().contains(MimeTypeMap.getFileExtensionFromUrl(localPath).toLowerCase())

    override fun toString(): String {
        return "ImageInfo{" +
                ", dateTaken=" + dateTaken +
                ", localPath='" + localPath + '\''.toString() +
                ", orientation=" + orientation +
                '}'.toString()
    }
}