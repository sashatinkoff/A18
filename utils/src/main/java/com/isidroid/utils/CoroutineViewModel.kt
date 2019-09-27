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

    protected fun io(block: () -> Unit): Job {
        progress.postValue(true)

        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                Timber.e(e)
                error.postValue(e)
            } finally {
                progress.postValue(false)
            }
        }
    }
}