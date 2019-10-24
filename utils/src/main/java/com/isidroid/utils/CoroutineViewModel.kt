package com.isidroid.utils

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import timber.log.Timber

abstract class CoroutineViewModel(application: Application) : AndroidViewModel(application) {
    val error by lazy { MutableLiveData<Throwable>() }
    val progress by lazy { MutableLiveData<Boolean>() }

    protected fun <T> io(
        doWork: () -> T,
        doBefore: (() -> Unit)? = null,
        onComplete: ((T?) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        mainDispatcher: MainCoroutineDispatcher = Dispatchers.Main,
        workDispatcher: CoroutineDispatcher = Dispatchers.IO,
        forceIfNoActive: Boolean = false
    ): Job? {
        if (!viewModelScope.isActive && forceIfNoActive) {
            return Tasks.io(
                mainDispatcher = mainDispatcher,
                workDispatcher = workDispatcher,
                doBefore = doBefore,
                doWork = doWork,
                onComplete = onComplete,
                onError = {
                    error.postValue(it)
                    onError?.invoke(it)
                }
            )
        }

        return viewModelScope.launch(mainDispatcher) {
            progress.postValue(true)

            var data: T? = null
            try {
                doBefore?.invoke()
                withContext(workDispatcher) { data = doWork() }
                onComplete?.invoke((data))
            } catch (e: Throwable) {
                Timber.e(e)
                error.postValue(e)
                onError?.invoke(e)
            } finally {
                progress.postValue(false)
            }
        }
    }

    protected fun <T> io(doWork: () -> T) = io(
        doWork = doWork,
        onComplete = null
    )

    protected fun a() {
        viewModelScope.ensureActive()
    }
}