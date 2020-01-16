package com.isidroid.a18.utils

import timber.log.Timber

object Utils {
    fun tryCatch(block: () -> Unit, onError: (Throwable) -> Unit = {}, logError: Boolean = false, finally: () -> Unit = {}) {
        try {
            block()
        } catch (e: Exception) {
            if (logError) Timber.e(e)
            onError(e)
        } finally {
            finally()
        }
    }
}