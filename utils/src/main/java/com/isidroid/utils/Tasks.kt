package com.isidroid.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Tasks {
    fun <T> io(
        doWork: () -> T,
        onComplete: ((T?) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) = GlobalScope.launch(Dispatchers.Main) {
        var data: T? = null
        try {
            withContext(Dispatchers.IO) { data = doWork() }
            onComplete?.invoke((data))
        } catch (e: Throwable) {
            onError?.invoke(e)
        }
    }
}