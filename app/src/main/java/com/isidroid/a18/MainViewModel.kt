package com.isidroid.a18

import android.app.Application
import com.isidroid.a18.sample.rest.ApiTest
import com.isidroid.utils.CoroutineViewModel
import timber.log.Timber

class MainViewModel(application: Application) : CoroutineViewModel(application) {
    fun posts() = io(
        doWork = {
            Timber.tag("sdfsdfsdf").i("dowork on ${Thread.currentThread().name}")
            ApiTest.create().posts().execute()
        },
        onComplete = {
            Timber.tag("sdfsdfsdf").i("onComplete size=${it?.body()?.size} on ${Thread.currentThread().name}")
        },
        onError = {
            Timber.tag("sdfsdfsdf").e("${it.message} on ${Thread.currentThread().name}")
        }
    )
}