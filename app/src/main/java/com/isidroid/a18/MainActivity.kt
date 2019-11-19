package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.sample.rest.ApiTest
import com.isidroid.perms.askPermission
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import com.isidroid.utils.extensions.onKeyboardVisibility
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val repository by lazy { LocationRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start()

        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {

        }

        btnRefreshed.setOnClickListener { refresh() }
        btnStart.setOnClickListener { start() }
        btnStop.setOnClickListener { stop() }
    }

    private fun refresh() {
        repository.start(
            useLast = false,
            onLocation = { Timber.i("onLocation $it") },
            onError = { Timber.e(it.message) }
        )
    }

    private fun start() {
        Tasks.io(
            doWork = { ApiTest.create().posts().execute().body() },
            onError = { Timber.tag("Uosj").e(it) },
            onComplete = { Timber.tag("Uosj").i("result=${it?.firstOrNull()?.userId}") }
        )
    }

    private fun stop() {
        repository.stop()
    }
}