package com.isidroid.pics

import android.graphics.Bitmap
import java.io.Serializable

import java.util.Date

class Result : Serializable {
    var bitmap: Bitmap? = null
    var dateTaken: Date? = null
    var localPath: String? = null
    var orientation: Int = 0

    override fun toString(): String {
        return "Result{" +
                "bitmap=" + bitmap +
                ", dateTaken=" + dateTaken +
                ", localPath='" + localPath + '\''.toString() +
                ", orientation=" + orientation +
                '}'.toString()
    }
}