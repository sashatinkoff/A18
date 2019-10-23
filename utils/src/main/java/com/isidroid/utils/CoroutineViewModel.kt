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
        workDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) = viewModelScope.launch(mainDispatcher) {
        progress.postValue(true)
        doBefore?.invoke()
        var data: T? = null
        try {
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

    protected fun <T> io(doWork: () -> T) = io(
        doWork = doWork,
        onComplete = null
    )
}