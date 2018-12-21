package com.isidroid.realm

import io.reactivex.Flowable

fun <T> Flowable<T>.realm(): Flowable<T> = doOnNext { YRealm.refresh() }