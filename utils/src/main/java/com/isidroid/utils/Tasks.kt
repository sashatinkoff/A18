package com.isidroid.utils

import kotlinx.coroutines.*

object Tasks {
    fun <T> io(
        doWork: () -> T,
        onComplete: ((T?) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        contextMain: CoroutineDispatcher = Dispatchers.Main,
        contextWorker: CoroutineDispatcher = Dispatchers.IO
    ) = GlobalScope.launch(contextMain) {
        var data: T? = null
        try {
            withContext(contextWorker) { data = doWork() }
            onComplete?.invoke((data))
        } catch (e: Throwable) {
            onError?.invoke(e)
        }
    }
}