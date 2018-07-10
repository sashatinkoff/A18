package com.isidroid.utilsmodule

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface RxDisposableContainer {
    var disposable: Disposable?

    @CallSuper
    fun cancel() {
        disposable?.dispose()
    }
}

fun RxDisposableContainer.addToDisposableContainer(disposable: Disposable, compositeDisposable: CompositeDisposable? = null) {
    this.disposable = compositeDisposable?.let { disposable.addTo(it) }
}