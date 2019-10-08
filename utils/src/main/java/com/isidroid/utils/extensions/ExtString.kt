package com.isidroid.utils.extensions

import java.security.MessageDigest

fun String.md5() = MessageDigest.getInstance("MD5")
    .digest(toByteArray())
    .joinToString("") { String.format("%02x", it) }