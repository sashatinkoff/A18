package com.isidroid.a18

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.isidroid.a18.sample.rest.ApiTest
import com.isidroid.utils.CoroutineViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import timber.log.Timber

class MainViewModel(application: Application) : CoroutineViewModel(application) {
    fun posts() = io(
        doWork = {
            Timber.tag("sdfsdfsdf").i("dowork on ${Thread.currentThread().name}")
            ApiTest.create().posts().execute()
        },
        onComplete = {
            Timber.tag("sdfsdfsdf")
                .i("onComplete size=${it?.body()?.size} on ${Thread.currentThread().name}")
        },
        onError = {
            Timber.tag("sdfsdfsdf").e("${it.message} on ${Thread.currentThread().name}")
        }
    )

    fun start() = io(
        forceIfNoActive = true,
        doWork = {
            (0 until 2).forEach {
                Timber.i("pass $it active=${viewModelScope.isActive}")
                Thread.sleep(1_000)
            }
        },
        onError = { Timber.e(it.message) }
    )

    fun stop() {
        viewModelScope.cancel("Manual")
    }
}