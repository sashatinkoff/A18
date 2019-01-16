package com.isidroid.pics

import java.io.Serializable
import java.util.*

class Result : Serializable {
    var dateTaken: Date? = null
    var localPath: String? = null
    var orientation: Int = 0

    override fun toString(): String {
        return "Result{" +
                ", dateTaken=" + dateTaken +
                ", localPath='" + localPath + '\''.toString() +
                ", orientation=" + orientation +
                '}'.toString()
    }
}