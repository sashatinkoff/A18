package com.isidroid.utils

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

abstract class CoroutineViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Main

    val error by lazy { MutableLiveData<Throwable>() }
    val progress by lazy { MutableLiveData<Boolean>() }

    protected fun io(executor: () -> Unit) {
        val dispatcher: CoroutineDispatcher = Dispatchers.IO
        progress.postValue(true)
        launch {
            withContext(dispatcher) {
                try {
                    executor()
                } catch (e: Exception) {
                    error.postValue(e)
                } finally {
                    progress.postValue(false)
                }
            }
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}