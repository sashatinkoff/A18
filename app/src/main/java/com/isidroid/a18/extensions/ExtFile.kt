package com.isidroid.a18.extensions

import java.io.File
import java.io.InputStream

fun InputStream.saveToFile(file: File) = use { input ->
    file.outputStream().use { output -> input.copyTo(output) }
}