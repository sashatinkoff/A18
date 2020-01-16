package com.isidroid.a18.extensions

import java.security.MessageDigest
import kotlin.math.ln

fun Long.withSuffix(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format(
        "%.1f%c",
        this / Math.pow(1000.0, exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}

fun Int.withSuffix() = this.toLong().withSuffix()
fun String.short(limit: Int = 20) =
    if (length < limit) this else "${substring(0, limit).trim()}..."

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString("") {
        String.format("%02x", it)
    }
}