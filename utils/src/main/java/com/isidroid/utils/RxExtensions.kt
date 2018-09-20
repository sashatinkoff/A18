package com.isidroid.utils

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}

fun <T> Flowable<T>.uiSubscribe(): Flowable<T> {
    return subscribeBack().subscribeUi()
}

fun <T> Flowable<T>.subscribeBack(): Flowable<T> {
    return subscribeOn(Schedulers.io())
}

fun <T> Flowable<T>.subscribeUi(): Flowable<T> {
    return observeOn(AndroidSchedulers.mainThread())
}