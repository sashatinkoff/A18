package com.isidroid.utils

import kotlinx.coroutines.*
import timber.log.Timber

object Tasks {
    fun <T> io(
        doBefore: (() -> Unit)? = null,
        doWork: () -> T,
        onComplete: ((T?) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) = GlobalScope.launch(mainDispatcher) {
        var data: T? = null
        try {
            doBefore?.invoke()
            withContext(workDispatcher) { data = doWork() }
            onComplete?.invoke((data))
        } catch (e: Throwable) {
            Timber.e(e)
            onError?.invoke(e)
        }
    }
}