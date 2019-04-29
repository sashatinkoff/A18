package com.isidroid.utils

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

open class BaseViewModel : ViewModel() {
    private var isProgressInitialized = false
    protected val compositeDisposable = CompositeDisposable()

    val error = MutableLiveData<Throwable>()
    val progress by lazy {
        isProgressInitialized = true
        MutableLiveData<Boolean>()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun keepProgress() = apply { isProgressInitialized = false }
    protected open fun postResult(t: Throwable?, callback: (() -> Unit)? = null) {
        if (t != null) {
            Timber.e(t)
            progress.postValue(false)
            error.postValue(t)
        } else {
            if (isProgressInitialized) {
                progress.postValue(false)
                isProgressInitialized = true
            }

            callback?.invoke()
        }
    }
}