package com.isidroid.utils

import android.os.Looper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}

fun <T> Flowable<T>.subscribeIoMain(): Flowable<T> {
    return subscribeIo().subscribeMain()
}

fun <T> Flowable<T>.subscribeMainIO(): Flowable<T> {
    return subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io())
}

fun <T> Flowable<T>.subscribeIo(): Flowable<T> {
    return subscribeOn(Schedulers.io())
}

fun <T> Flowable<T>.subscribeMain(): Flowable<T> {
    return observeOn(AndroidSchedulers.mainThread())
}