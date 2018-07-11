package com.isidroid.utilsmodule

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface RxDisposableContainer {
    var disposable: Disposable?
    val compositeDisposable: CompositeDisposable


    @CallSuper
    fun cancel() {
        disposable?.dispose()
        onCancel(disposable?.isDisposed == true)
    }

    fun onCancel(isCanceled: Boolean) {}
}

