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

    fun start() = io(
        doWork = { ApiTest.create().posts().execute().body() },
        onError = { Timber.tag("Uosj").e("err=${it}, error=${error.value}") },
        onComplete = { Timber.tag("Uosj").i("result=${it?.firstOrNull()?.userId}") }
    )

}